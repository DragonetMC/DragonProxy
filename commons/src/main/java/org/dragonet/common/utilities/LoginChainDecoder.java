package org.dragonet.common.utilities;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.common.io.ByteStreams;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import org.dragonet.common.data.entity.Skin;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.interfaces.ECPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created on 2017/9/12.
 */
public class LoginChainDecoder {

    private static final String ENCODED_ROOT_CA_KEY;
    private static final KeyFactory EC_KEY_FACTORY;

    private final byte[] chainJWT;
    private final byte[] clientDataJWT;
    public String username;
    public UUID clientUniqueId;
    public JsonObject clientData;
    public String language;
    public Skin skin;
    private boolean loginVerified = false;

    public LoginChainDecoder(byte[] chainJWT, byte[] clientDataJWT) {
        this.chainJWT = chainJWT;
        this.clientDataJWT = clientDataJWT;
    }

    /**
     * decode the chain data in Login packet for MCPE Note: the credit of this
     * function goes to Nukkit development team
     */
    public void decode() {

        Gson gson = new Gson();

        Map<String, List<String>> map = gson.fromJson(new String(this.chainJWT, StandardCharsets.UTF_8),
            new TypeToken<Map<String, List<String>>>() {
            }.getType());
        if (map.isEmpty() || !map.containsKey("chain") || map.get("chain").isEmpty())
            return;

        DecodedJWT clientJWT = JWT.decode(new String(this.clientDataJWT, StandardCharsets.UTF_8));

        List<DecodedJWT> chainJWTs = new ArrayList<>();

        // Add the JWT tokens to a chain
        for (String token : map.get("chain"))
            chainJWTs.add(JWT.decode(token));

        chainJWTs.add(clientJWT);

        try {

            ECPublicKey prevPublicKey = null;

            for (DecodedJWT jwt : chainJWTs) {

                JsonObject payload = gson.fromJson(new String(Base64.getDecoder().decode(jwt.getPayload())), JsonObject.class);

                String encodedPublicKey = null;
                ECPublicKey publicKey = null;

                if (payload.has("identityPublicKey")) {

                    encodedPublicKey = payload.get("identityPublicKey").getAsString();

                    publicKey = (ECPublicKey) EC_KEY_FACTORY.generatePublic(
                        new X509EncodedKeySpec(Base64.getDecoder().decode(encodedPublicKey))
                    );

                }

                // Trust the root ca public key and use it to verify the chain
                if (ENCODED_ROOT_CA_KEY.equals(encodedPublicKey) && payload.has("certificateAuthority")
                    && payload.get("certificateAuthority").getAsBoolean()) {
                    prevPublicKey = publicKey;
                    continue;
                }

                // This will happen if the root ca key we have does not match the one presented by the client chain
                if (prevPublicKey == null)
                    throw new NullPointerException("No trusted public key found in chain");

                // Throws a SignatureVerificationException if the verification failed
                Algorithm.ECDSA384(prevPublicKey, null).verify(jwt);

                // Verification was successful since no exception was thrown

                // Set the previous public key to this one so that it can be used
                // to verify the next JWT token in the chain
                prevPublicKey = publicKey;

            }

            // The for loop successfully verified all JWT tokens with no exceptions thrown
            this.loginVerified = true;

            System.out.println("The LoginPacket has been successfully verified for integrity");

        } catch (Exception e) {
            this.loginVerified = false;
            System.out.println("Failed to verify the integrity of the LoginPacket");
            e.printStackTrace();
        }

        // This is in its own for loop due to the possibility that the chain verification failed
        for (DecodedJWT jwt : chainJWTs) {

            JsonObject payload = gson.fromJson(new String(Base64.getDecoder().decode(jwt.getPayload())), JsonObject.class);

            // Get the information we care about - The UUID and display name
            if (payload.has("extraData") && !payload.has("certificateAuthority")) {
                JsonObject extra = payload.get("extraData").getAsJsonObject();
                if (extra.has("displayName")) {
                    this.username = extra.get("displayName").getAsString();
                }
                if (extra.has("identity")) {
                    this.clientUniqueId = UUID.fromString(extra.get("identity").getAsString());
                }

                break;
            }

        }

        // client data
        this.clientData = gson.fromJson(new String(Base64.getDecoder().decode(clientJWT.getPayload()), StandardCharsets.UTF_8), JsonObject.class);

        this.language = this.clientData.has("LanguageCode") ? this.clientData.get("LanguageCode").getAsString() : "en_US";

        this.skin = new Skin(
            this.clientData.get("SkinId").getAsString(),
            this.clientData.has("SkinData") ? Base64.getDecoder().decode(this.clientData.get("SkinData").getAsString().replace("-_", "+/")) : new byte[0],
            this.clientData.has("CapeData") ? Base64.getDecoder().decode(this.clientData.get("CapeData").getAsString().replace("-_", "+/")) : new byte[0],
            this.clientData.get("SkinGeometryName").getAsString(),
            this.clientData.has("SkinGeometry") ? Base64.getDecoder().decode(this.clientData.get("SkinGeometry").getAsString().replace("-_", "+/")) : new byte[0]
        );
    }

    public boolean isLoginVerified() {
        return this.loginVerified;
    }

    static {

        try (InputStream in = LoginChainDecoder.class.getResourceAsStream("/mojang_root_public.key")) {

            ENCODED_ROOT_CA_KEY = new String(ByteStreams.toByteArray(in), StandardCharsets.UTF_8);

            in.close();

            // Java 7 fully implements elliptic curve crypto
            EC_KEY_FACTORY = KeyFactory.getInstance("EC");

        } catch (Exception e) {
            e.printStackTrace();
            // Throw a runtime exception to indicate that this exception should never happen
            // Possible causes include the key file missing and EC not supported
            // Though EC is supported in Java 7, and the key file is embedded into the jar
            throw new RuntimeException(e);
        }

    }

}
