package org.dragonet.proxy.nbt;

import org.dragonet.proxy.nbt.stream.NBTInputStream;
import org.dragonet.proxy.nbt.stream.NBTOutputStream;
import org.dragonet.proxy.nbt.tag.CompoundTag;
import org.dragonet.proxy.nbt.tag.Tag;

import java.io.*;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Collection;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class PENBT {
    /**
     * A Named Binary Tag library for Nukkit Project
     */
    public static CompoundTag read(File file) throws IOException {
        return read(file, ByteOrder.BIG_ENDIAN);
    }

    public static CompoundTag read(File file, ByteOrder endianness) throws IOException {
        if (!file.exists()) return null;
        return read(new FileInputStream(file), endianness);
    }

    public static CompoundTag read(InputStream inputStream) throws IOException {
        return read(inputStream, ByteOrder.BIG_ENDIAN);
    }

    public static CompoundTag read(InputStream inputStream, ByteOrder endianness) throws IOException {
        NBTInputStream stream = new NBTInputStream(inputStream, endianness);
        try {
            Tag tag = Tag.readNamedTag(stream);
            if (tag instanceof CompoundTag) {
                return (CompoundTag) tag;
            }
            throw new IOException("Root tag must be a named compound tag");
        } finally {
            stream.close();
        }
    }

    public static CompoundTag read(byte[] data) throws IOException {
        return read(data, ByteOrder.BIG_ENDIAN);
    }

    public static CompoundTag read(byte[] data, ByteOrder endianness) throws IOException {
        return read(new ByteArrayInputStream(data), endianness);
    }

    public static CompoundTag readCompressed(InputStream inputStream) throws IOException {
        return readCompressed(inputStream, ByteOrder.BIG_ENDIAN);
    }

    public static CompoundTag readCompressed(InputStream inputStream, ByteOrder endianness) throws IOException {
        return read(new BufferedInputStream(new GZIPInputStream(inputStream)), endianness);
    }

    public static CompoundTag readCompressed(byte[] data) throws IOException {
        return readCompressed(data, ByteOrder.BIG_ENDIAN);
    }

    public static CompoundTag readCompressed(byte[] data, ByteOrder endianness) throws IOException {
        return read(new BufferedInputStream(new GZIPInputStream(new ByteArrayInputStream(data))), endianness);
    }

    public static byte[] write(CompoundTag tag) throws IOException {
        return write(tag, ByteOrder.BIG_ENDIAN);
    }

    public static byte[] write(CompoundTag tag, ByteOrder endianness) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        NBTOutputStream stream = new NBTOutputStream(baos, endianness);
        try {
            Tag.writeNamedTag(tag, stream);
            return baos.toByteArray();
        } finally {
            stream.close();
        }
    }

    public static byte[] write(Collection<CompoundTag> tags) throws IOException {
        return write(tags, ByteOrder.BIG_ENDIAN);
    }

    public static byte[] write(Collection<CompoundTag> tags, ByteOrder endianness) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        NBTOutputStream stream = new NBTOutputStream(baos, endianness);
        try {
            for (CompoundTag tag : tags) {
                Tag.writeNamedTag(tag, stream);
            }
            return baos.toByteArray();
        } finally {
            stream.close();
        }
    }

    public static void write(CompoundTag tag, File file) throws IOException {
        write(tag, file, ByteOrder.BIG_ENDIAN);
    }

    public static void write(CompoundTag tag, File file, ByteOrder endianness) throws IOException {
        write(tag, new FileOutputStream(file), endianness);
    }

    public static void write(CompoundTag tag, OutputStream outputStream) throws IOException {
        write(tag, outputStream, ByteOrder.BIG_ENDIAN);
    }

    public static void write(CompoundTag tag, OutputStream outputStream, ByteOrder endianness) throws IOException {
        NBTOutputStream stream = new NBTOutputStream(outputStream, endianness);
        try {
            Tag.writeNamedTag(tag, stream);
        } finally {
            stream.close();
        }
    }

    public static byte[] writeGZIPCompressed(CompoundTag tag) throws IOException {
        return writeGZIPCompressed(tag, ByteOrder.BIG_ENDIAN);
    }

    public static byte[] writeGZIPCompressed(CompoundTag tag, ByteOrder endianness) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        writeGZIPCompressed(tag, baos, endianness);
        return baos.toByteArray();
    }

    public static void writeGZIPCompressed(CompoundTag tag, OutputStream outputStream) throws IOException {
        writeGZIPCompressed(tag, outputStream, ByteOrder.BIG_ENDIAN);
    }

    public static void writeGZIPCompressed(CompoundTag tag, OutputStream outputStream, ByteOrder endianness) throws IOException {
        write(tag, new GZIPOutputStream(outputStream), endianness);
    }

    public static void writeZLIBCompressed(CompoundTag tag, OutputStream outputStream) throws IOException {
        writeZLIBCompressed(tag, outputStream, ByteOrder.BIG_ENDIAN);
    }

    public static void writeZLIBCompressed(CompoundTag tag, OutputStream outputStream, ByteOrder endianness) throws IOException {
        writeZLIBCompressed(tag, outputStream, Deflater.DEFAULT_COMPRESSION, endianness);
    }

    public static void writeZLIBCompressed(CompoundTag tag, OutputStream outputStream, int level) throws IOException {
        writeZLIBCompressed(tag, outputStream, level, ByteOrder.BIG_ENDIAN);
    }

    public static void writeZLIBCompressed(CompoundTag tag, OutputStream outputStream, int level, ByteOrder endianness) throws IOException {
        write(tag, new DeflaterOutputStream(outputStream, new Deflater(level)), endianness);
    }

    public static void safeWrite(CompoundTag tag, File file) throws IOException {
        File tmpFile = new File(file.getAbsolutePath() + "_tmp");
        if (tmpFile.exists()) {
            tmpFile.delete();
        }
        write(tag, tmpFile);
        Files.move(tmpFile.toPath(), file.toPath(), StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
    }

}
