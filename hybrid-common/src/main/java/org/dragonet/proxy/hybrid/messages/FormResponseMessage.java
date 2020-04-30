package org.dragonet.proxy.hybrid.messages;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.dragonet.proxy.hybrid.HybridMessage;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FormResponseMessage implements HybridMessage {
    private int formId;
    private String formData;

    @Override
    public ByteArrayDataOutput encode(ByteArrayDataOutput out) {
        out.writeInt(formId);
        out.writeUTF(formData);
        return out;
    }

    @Override
    public void decode(ByteArrayDataInput in) {
        formId = in.readInt();
        formData = in.readUTF();
    }

    @Override
    public String getId() {
        return "FormResponse";
    }
}
