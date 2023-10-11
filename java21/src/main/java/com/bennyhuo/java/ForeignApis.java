package com.bennyhuo.java;

import java.lang.foreign.*;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.invoke.VarHandle;
import java.lang.reflect.Field;
import sun.misc.Unsafe;

/**
 * Created by benny.
 */
public class ForeignApis {

    static {
        System.loadLibrary("libsimple");
    }

    public static void main(String[] args) {
        // useUnsafe();
//        useNewMemoryApi();
        downCall();
        upCall();
        callSystem();
    }

    public static void onEach(int element) {
        System.out.print(STR."\{element}, ");
    }

    public static void downCall() {
        try {
            Linker linker = Linker.nativeLinker();
            SymbolLookup loaderLookup = SymbolLookup.loaderLookup();

            MemorySegment getCLangVersion = loaderLookup.find("GetCLangVersion").get();
            // 方法二
            // MemorySegment getCLangVersion = SymbolLookup.libraryLookup("libsimple", Arena.ofAuto()).find("GetCLangVersion").get();
            MethodHandle getClangVersionHandle = linker.downcallHandle(getCLangVersion, FunctionDescriptor.of(ValueLayout.JAVA_INT));

            System.out.println(getClangVersionHandle.invoke());

            MemoryLayout personLayout = MemoryLayout.structLayout(
                    ValueLayout.JAVA_LONG.withName("id"),
                    MemoryLayout.sequenceLayout(10, ValueLayout.JAVA_BYTE).withName("name"),
                    MemoryLayout.paddingLayout(2),
                    ValueLayout.JAVA_INT.withName("age"));

            MemorySegment person = Arena.ofAuto().allocate(personLayout);
            VarHandle idHandle = personLayout.varHandle(MemoryLayout.PathElement.groupElement("id"));
            idHandle.set(person, 1000000);

            // 方法一
            VarHandle nameHandle = personLayout.varHandle(
                    MemoryLayout.PathElement.groupElement("name"),
                    MemoryLayout.PathElement.sequenceElement()
            );
            byte[] bytes = "bennyhuo".getBytes();
            for (int i = 0; i < bytes.length; i++) {
                nameHandle.set(person, i, bytes[i]);
            }
            nameHandle.set(person, bytes.length, (byte) 0);

            // 方法二
            person.asSlice(personLayout.byteOffset(MemoryLayout.PathElement.groupElement("name")))
                    .copyFrom(Arena.ofAuto().allocateUtf8String("BennyHuo"));

            VarHandle ageHandle = personLayout.varHandle(MemoryLayout.PathElement.groupElement("age"));
            ageHandle.set(person, 30);

            MemorySegment dumpPerson = loaderLookup.find("DumpPerson").get();
            MethodHandle dumpPersonHandle = linker.downcallHandle(
                    dumpPerson,
                    FunctionDescriptor.ofVoid(AddressLayout.ADDRESS)
            );

            dumpPersonHandle.invoke(person);

            for (byte b : person.toArray(ValueLayout.JAVA_BYTE)) {
                System.out.printf("%x, ", b);
            }
            System.out.println();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static void upCall() {
        try {
            Linker linker = Linker.nativeLinker();
            MethodHandle onEachHandle = MethodHandles.lookup()
                    .findStatic(ForeignApis.class, "onEach",
                            MethodType.methodType(void.class, int.class));

            MemorySegment onEachHandleAddress = linker.upcallStub(
                    onEachHandle, FunctionDescriptor.ofVoid(ValueLayout.JAVA_INT), Arena.ofAuto()
            );

            int[] originalArray = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
            MemorySegment array = Arena.ofAuto().allocate(4 * 10);
            array.copyFrom(MemorySegment.ofArray(originalArray));

            SymbolLookup loaderLookup = SymbolLookup.loaderLookup();
            MemorySegment forEach = loaderLookup.find("ForEach").get();
            MethodHandle forEachHandle = linker.downcallHandle(
                    forEach,
                    FunctionDescriptor.ofVoid(AddressLayout.ADDRESS, ValueLayout.JAVA_INT, AddressLayout.ADDRESS)
            );

            System.out.print("onEach: ");
            forEachHandle.invoke(array, originalArray.length, onEachHandleAddress);
            System.out.println();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static void callSystem() {
        try {
            Linker linker = Linker.nativeLinker();
            MemorySegment strlen = linker.defaultLookup().find("strlen").get();
            MethodHandle strlenHandle = linker.downcallHandle(strlen,
                    FunctionDescriptor.of(ValueLayout.JAVA_LONG, AddressLayout.ADDRESS));

            MemorySegment string = Arena.ofAuto().allocateUtf8String("Hello World!!");
            System.out.println(strlenHandle.invoke(string));
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static void useNewMemoryApi() {
        {
            MemorySegment segment = Arena.ofAuto().allocate(100);
            for (int i = 0; i < 25; i++) {
                segment.setAtIndex(ValueLayout.JAVA_INT, i, i);
            }

            for (int i = 0; i < 25; i++) {
                System.out.print(segment.getAtIndex(ValueLayout.JAVA_INT, i));
            }

            System.out.println();
        }

        {
            MemorySegment segment = Arena.ofAuto().allocate(100);
            SequenceLayout layout = MemoryLayout.sequenceLayout(ValueLayout.JAVA_INT);
            VarHandle handle = layout.varHandle(MemoryLayout.PathElement.sequenceElement());
            for (int i = 0; i < 25; i++) {
                handle.set(segment, i, i);
            }

            for (int i = 0; i < 25; i++) {
                System.out.print(segment.getAtIndex(ValueLayout.JAVA_INT, i));
            }

            System.out.println();
        }

        {
            MemorySegment segment = Arena.ofAuto().allocate(100);
            VarHandle intHandle = ValueLayout.JAVA_INT.varHandle();
            for (int i = 0; i < 25; i++) {
                intHandle.set(segment.asSlice(i * 4), /* value to write */ i);
            }

            for (int i = 0; i < 25; i++) {
                System.out.print(intHandle.get(segment.asSlice(i * 4)));
            }

            System.out.println();

            for (int i = 0; i < 25; i++) {
                System.out.print(segment.getAtIndex(ValueLayout.JAVA_INT, i));
            }

            System.out.println();
        }

        {
            SequenceLayout intArrayLayout = MemoryLayout.sequenceLayout(25, ValueLayout.JAVA_INT);
            MemorySegment segment = Arena.ofAuto().allocate(intArrayLayout);
            VarHandle indexedElementHandle = intArrayLayout.varHandle(MemoryLayout.PathElement.sequenceElement());
            for (int i = 0; i < intArrayLayout.elementCount(); i++) {
                indexedElementHandle.set(segment, (long) i, i);
            }

            for (int i = 0; i < 25; i++) {
                System.out.print(segment.getAtIndex(ValueLayout.JAVA_INT, i));
            }

            System.out.println();
        }
    }

    public static void useUnsafe() {
        try {
            Field theUnsafeField = Unsafe.class.getDeclaredField("theUnsafe");
            theUnsafeField.setAccessible(true);
            Unsafe unsafe = (Unsafe) theUnsafeField.get(null);
            long handle = unsafe.allocateMemory(16);

            unsafe.putDouble(handle, 1024);
            System.out.println(unsafe.getDouble(handle));

            unsafe.putInt(handle + 16, 1000);
            System.out.println(unsafe.getInt(handle + 16));

            unsafe.freeMemory(handle);
            System.out.println(unsafe.getInt(handle));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

