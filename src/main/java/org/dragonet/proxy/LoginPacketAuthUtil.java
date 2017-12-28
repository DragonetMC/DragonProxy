package org.dragonet.proxy;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.common.io.ByteStreams;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import org.dragonet.proxy.protocol.packets.LoginPacket;

import java.io.InputStream;
import java.security.KeyFactory;
import java.security.interfaces.ECPublicKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.List;
import java.util.Map;

public class LoginPacketAuthUtil {

	private static final Algorithm VERIFY_ALGORITHM;

	public static boolean verifyLogin(LoginPacket packet) {

		System.out.println("Verifying login packet...");

		try {

			//debug - data dump
			//Files.write(Paths.get("chain_jwt.txt"), packet.decoded.chainJWT);
			//Files.write(Paths.get("client_jwt.txt"), packet.decoded.clientDataJWT);

			Map<String, List<String>> map = new Gson().fromJson(new String(packet.decoded.chainJWT),
					new TypeToken<Map<String, List<String>>>() {
					}.getType());
			if (map.isEmpty() || !map.containsKey("chain") || map.get("chain").isEmpty()) {
				return false;
			}

			List<String> chains = map.get("chain");

			DecodedJWT toVerify = null;

			for (String c : chains) {

				DecodedJWT jwt = JWT.decode(c);
				JsonObject object = new Gson().fromJson(new String(Base64.getDecoder().decode(jwt.getPayload())), JsonObject.class);

				if (object.has("extraData")) {
					toVerify = jwt;
					break;
				}

			}

			if (toVerify != null) {

				VERIFY_ALGORITHM.verify(toVerify);

				System.out.println("Verification was successful");

				return true;

			} else {
				System.out.println("Failed to find chain data for extra data");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	static {

		try {

			System.out.println("Loading JWT algorithm...");

			// Read bytes for public key
			InputStream in = LoginPacketAuthUtil.class.getResourceAsStream("/mojang_immediate_public.key");

			byte[] keyBytes = Base64.getDecoder().decode(ByteStreams.toByteArray(in));

			in.close();

			//Create a new public key instance
			KeyFactory kf = KeyFactory.getInstance("EC");
			EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
			ECPublicKey publicKey = (ECPublicKey) kf.generatePublic(keySpec);

			//Set up the algorithm to verify JWT signature
			VERIFY_ALGORITHM = Algorithm.ECDSA384(publicKey, null);

			System.out.println("Successfully loaded JWT verification algorithm");

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

	}

}
