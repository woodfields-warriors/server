java_binary(
  name = "main",
  srcs = glob(["src/main/*.java"]),
  deps = [":auth_service", ":game_service"],
  main_class = "com.wwttr.main.Main",
)

java_proto_library(
  name = "auth_api",
  deps = [":auth_proto"],
)

proto_library(
  name = "auth_proto",
  srcs = ["src/auth/api.proto"],
)

java_library(
  name = "auth_service",
  srcs = glob(["src/auth/*.java"]),
  deps = [":auth_api"],
)

java_test(
  name = "auth",
  test_class = "com.wwttr.auth.AuthServiceTest",
  srcs = glob(["test/auth/*.java"]),
  deps = [
    ":auth_service",
    ":auth_api"
  ]
)

java_proto_library(
  name = "game_api",
  deps = [":game_proto"],
)

proto_library(
  name = "game_proto",
  srcs = ["src/game/api.proto"],
)

java_library(
  name = "game_service",
  srcs = glob(["src/game/*.java"]),
  deps = [":game_api"],
)

java_test(
  name = "game",
  test_class = "com.wwttr.game.GameServiceTest",
  srcs = glob([
    "test/game/*.java",
    ]),
  deps = [
    ":game_service",
    ":game_api",
  ]
)
