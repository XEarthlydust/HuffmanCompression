package top.xearthlydust.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BitUtil {

    private static final int BYTE_SIZE = 8;

    public static byte[] getCodeByTable(Map<Byte, Byte[]> codeTable, byte[] bytes) {
        byte currentByte = 0;
        byte numBitsFilled = 0;
        List<Byte> saved = new ArrayList<>();

        for (byte aByte : bytes) {
            Byte[] rawEncodeByte = codeTable.get(aByte);
            byte bitCount = rawEncodeByte[0];
            byte value = rawEncodeByte[1];

            for (int i = bitCount - 1; i >= 0; i--) {
                if (((1 << i) & value) != 0) {
                    currentByte = (byte) (currentByte << 1 | 1);
                } else {
                    currentByte = (byte) (currentByte << 1);
                }
                numBitsFilled++;
                if (save(numBitsFilled, currentByte, saved)) {
                    numBitsFilled = 0;
                    currentByte = 0;
                }
            }
        }

        if (numBitsFilled != 0) {
            saved.add((byte) (currentByte << (8 - numBitsFilled)));
        }
        return toByteArray(saved, numBitsFilled);
    }

    private static boolean save(byte numBitsFilled, byte currentByte, List<Byte> saved) {
        if (numBitsFilled >= BYTE_SIZE) {
            saved.add(currentByte);
            return true;
        } else return false;
    }

    public static byte[] toByteArray(List<Byte> byteList, byte usedBit) {
        byte[] byteArray = new byte[byteList.size() + 1];
        for (int i = 0; i < byteList.size(); i++) {
            byteArray[i] = byteList.get(i);
        }
        byteArray[byteList.size()] = usedBit;
        return byteArray;
    }

    public static byte[] cutNull(byte[] bytes, int usedBit) {
        byte[] newBytes = new byte[usedBit];
        System.arraycopy(bytes, 0, newBytes, 0, usedBit);
        return newBytes;
    }
}
