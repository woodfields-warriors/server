// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: api.proto

package com.wwttr.api;

public final class Api {
  private Api() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_api_Request_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_api_Request_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_api_Response_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_api_Response_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\tapi.proto\022\003api\";\n\007Request\022\017\n\007service\030\001" +
      " \001(\t\022\016\n\006method\030\002 \001(\t\022\017\n\007payload\030\003 \001(\014\"E\n" +
      "\010Response\022\027\n\004code\030\001 \001(\0162\t.api.Code\022\017\n\007pa" +
      "yload\030\002 \001(\014\022\017\n\007message\030\003 \001(\t*\200\001\n\004Code\022\017\n" +
      "\013UNSPECIFIED\020\000\022\006\n\002OK\020\001\022\024\n\020INVALID_ARGUME" +
      "NT\020\002\022\014\n\010INTERNAL\020\003\022\017\n\013UNAVAILABLE\020\004\022\r\n\tN" +
      "OT_FOUND\020\005\022\021\n\rACCESS_DENIED\020\006\022\010\n\004PING\020\007B" +
      "\021\n\rcom.wwttr.apiP\001b\006proto3"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
        new com.google.protobuf.Descriptors.FileDescriptor.    InternalDescriptorAssigner() {
          public com.google.protobuf.ExtensionRegistry assignDescriptors(
              com.google.protobuf.Descriptors.FileDescriptor root) {
            descriptor = root;
            return null;
          }
        };
    com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        }, assigner);
    internal_static_api_Request_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_api_Request_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_api_Request_descriptor,
        new java.lang.String[] { "Service", "Method", "Payload", });
    internal_static_api_Response_descriptor =
      getDescriptor().getMessageTypes().get(1);
    internal_static_api_Response_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_api_Response_descriptor,
        new java.lang.String[] { "Code", "Payload", "Message", });
  }

  // @@protoc_insertion_point(outer_class_scope)
}