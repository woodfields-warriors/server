load("@io_bazel_rules_docker//java:image.bzl", "java_image")
load("@io_bazel_rules_docker//docker:docker.bzl", "docker_push")

### Main Binary ###

java_binary(
  name = "main",
  srcs = glob(["src/main/java/com/wwttr/main/*.java"]),
  deps = [
    ":server_lib",
    ":auth_service",
    ":auth_api",
    ":game_service",
    ":game_api",
    ":health_service",
    ":health_api",
    ":card_api",
    ":card_service",
    ":chat_api",
    ":chat_service",
    ],
  main_class = "com.wwttr.main.Main",
)

java_image(
  name = "main_docker",
  srcs = glob(["src/main/java/com/wwttr/main/*.java"]),
  deps = [
    ":server_lib",
    ":auth_service",
    ":auth_api",
    ":game_service",
    ":game_api",
    ":health_service",
    ":health_api",
    "@com_google_protobuf//:protobuf_java",
    ],
  main_class = "com.wwttr.main.Main",
  # layers = [":java_image_library"],
)

docker_push(
   name = "push",
   image = ":main_docker",
   registry = "gcr.io",
   repository = "ticket-to-ride-216915/server",
   tag = "$(REVISION_ID)",
)

### Server ###

java_library(
  name = "server_lib",
  srcs = glob(["src/main/java/com/wwttr/server/*.java"]),
  deps = [
    "@com_google_protobuf//:protobuf_java",
    ":models",
    ":api_lib",
    ":api_model",
  ],
)
java_test(
  name = "server",
  test_class = "com.wwttr.server.ServerTest",
  srcs = glob(["src/test/java/com/wwttr/server/*.java"]),
  deps = [
    ":server_lib",
    ":health_service",
    ":health_api",
    ":api_lib",
    ":api_model",
    "@com_google_protobuf//:protobuf_java",
  ]
)
java_library(
  name = "api_lib",
  srcs = glob(["src/main/java/com/wwttr/api/*.java"]),
  deps = [":api_model"],
)
java_proto_library(
  name = "api_model",
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
    ":models",
    ":database_lib",
    ":api_lib",
    ":api_model",
  ],
)

java_test(
  name = "auth",
  test_class = "com.wwttr.auth.AuthServiceTest",
  srcs = glob(["src/test/java/com/wwttr/auth/*.java"]),
  deps = [
    ":auth_service",
    ":auth_api",
    ":models",
    ":database_lib",
  ]
)

### Database ###

java_library(
  name = "database_lib",
  srcs = glob(["src/main/java/com/wwttr/database/*.java"]),
  deps = [
    "@com_google_protobuf//:protobuf_java",
    ":models",
    ":api_lib"
  ],
)

java_test(
  name = "database",
  test_class = "com.wwttr.database.DatabaseTest",
  srcs = glob(["src/test/java/com/wwttr/database/*.java"]),
  deps = [
    ":database_lib",
    ":models",
    ":api_lib"
  ]
)
#
# ## Player Service ###
#
# java_proto_library(
#   name = "player_api",
#   deps = [":player_proto"],
# )
# proto_library(
#   name = "player_proto",
#   srcs = ["src/main/proto/player.proto"],
# )

### Game Service ###

java_proto_library(
  name = "game_api",
  deps = [":game_proto"],
)
proto_library(
  name = "game_proto",
  srcs = ["src/main/proto/game.proto"],
  # deps = [":player_proto"],
)
java_library(
  name = "game_service",
  srcs = glob(["src/main/java/com/wwttr/game/*.java",
             "src/main/java/com/wwttr/models/*.java",
             "src/main/java/com/wwttr/database/*.java"]) ,
  deps = [
    ":game_api",
    "@com_google_protobuf//:protobuf_java",
    ":api_lib",
    "auth_service",
    ":api_model",
    ":card_service",
    ":chat_api",
    ":card_api"
  ],
)

java_library(
  name = "models",
  srcs = glob(["src/main/java/com/wwttr/models/*.java"]),
  deps = [
        ":api_model",
        ":card_api",
        ":chat_api",
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
    ":auth_service",
    ":api_lib",
  ]
)

### Health Service ###

java_library(
  name = "health_service",
  srcs = glob(["src/main/java/com/wwttr/health/*.java"]),
  deps = [
    ":health_api",
    "@com_google_protobuf//:protobuf_java",
    ":models",
  ],
)
# java_test(
#   name = "health",
#   test_class = "com.wwttr.health.HealthTest",
#   srcs = glob(["src/test/java/com/wwttr/health/*.java"]),
#   deps = [
#     ":auth_service",
#     ":auth_api"
#   ]
# )
java_proto_library(
  name = "health_api",
  deps = [":health_proto"],
)
proto_library(
  name = "health_proto",
  srcs = ["src/main/proto/health.proto"],
)

### Card Service ###

java_proto_library(
  name = "card_api",
  deps = [":card_proto"],
)

proto_library(
  name = "card_proto",
  srcs = ["src/main/proto/card.proto"],
)

java_library(
  name = "card_service",
  srcs = glob(["src/main/java/com/wwttr/card/*.java"]),
  deps = [
    ":card_api",
    ":models",
    ":api_model",
    ":api_lib",
    ":database_lib",
    "@com_google_protobuf//:protobuf_java"
  ],
)

java_test(
  name = "card",
  test_class = "com.wwttr.card.CardServiceTest",
  srcs = glob(["src/test/java/com/wwttr/card/*.java"]),
  deps = [
    "card_service",
    ":api_lib",
    ":database_lib",
    ":models",
    ":game_service",
    ":game_api"
  ]
)

### Chat Service ###

java_proto_library(
  name = "chat_api",
  deps = [":chat_proto"
          ],
)

proto_library(
  name = "chat_proto",
  srcs = ["src/main/proto/chat.proto"],
)

java_library(
  name = "chat_service",
  srcs = glob(["src/main/java/com/wwttr/chat/*.java"]),
  deps = [
    ":chat_api",
    ":models",
    ":api_model",
    ":database_lib",
    ":api_lib",
    "@com_google_protobuf//:protobuf_java",
  ],
)

java_test(
  name = "chat",
  test_class = "com.wwttr.chat.ChatServiceTest",
  srcs = glob(["src/test/java/com/wwttr/chat/*.java"]),
  deps = [
    "chat_service"
  ]
)
