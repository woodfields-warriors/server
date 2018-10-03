
### Main Binary ###

java_binary(
  name = "main",
  srcs = glob(["src/main/java/com/wwttr/main/*.java"]),
  deps = [
    ":auth_service",
    ":auth_api",
    ":game_service",
    ":game_api",
    ":api",
    "@com_google_protobuf//:protobuf_java",
    ],
  main_class = "com.wwttr.main.Main",
)
java_proto_library(
  name = "api",
  deps = [":api_proto"],
)
proto_library(
  name = "api_proto",
  srcs = ["src/main/proto/api.proto"],
)

### Auth Service ###

java_proto_library(
  name = "auth_api",
  deps = [":auth_proto"],
)
proto_library(
  name = "auth_proto",
  srcs = ["src/main/proto/auth.proto"],
)
java_library(
  name = "auth_service",
  srcs = glob(["src/main/java/com/wwttr/auth/*.java"]),
  deps = [
    ":auth_api",
    "@com_google_protobuf//:protobuf_java",
  ],
)
java_test(
  name = "auth",
  test_class = "com.wwttr.auth.AuthServiceTest",
  srcs = glob(["src/test/java/com/wwttr/auth/*.java"]),
  deps = [
    ":auth_service",
    ":auth_api"
  ]
)

### Game Service ###

java_proto_library(
  name = "game_api",
  deps = [":game_proto"],
)
proto_library(
  name = "game_proto",
  srcs = ["src/main/proto/game.proto"],
)
java_library(
  name = "game_service",
  srcs = glob(["src/main/java/com/wwttr/game/*.java"]),
  deps = [
    ":game_api",
    "@com_google_protobuf//:protobuf_java",

  ],
)
java_test(
  name = "game",
  test_class = "com.wwttr.game.GameServiceTest",
  srcs = glob([
    "src/test/java/com/wwttr/game/*.java",
    ]),
  deps = [
    ":game_service",
    ":game_api",
  ]
)
