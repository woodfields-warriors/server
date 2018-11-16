load("@io_bazel_rules_docker//java:image.bzl", "java_image")
load("@io_bazel_rules_docker//docker:docker.bzl", "docker_push")

### Main Binary ###

java_binary(
  name = "main",
  srcs = glob(["src/main/java/com/wwttr/main/*.java"]),
  deps = [
    ":server_lib",
    ":auth_service",
    ":game_service",
    ":health_service",
    ":card_service",
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
    ":game_service",
    ":health_service",
    ":card_service",
    ":chat_service",
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
  ],
)
java_test(
  name = "server",
  test_class = "com.wwttr.server.ServerTest",
  srcs = glob(["src/test/java/com/wwttr/server/*.java"]),
  deps = [
    ":server_lib",
    ":health_service",
    ":api_lib",
    "@com_google_protobuf//:protobuf_java",
  ]
)
java_library(
  name = "api_lib",
  srcs = glob(["src/main/java/com/wwttr/api/*.java"]),
  deps = [
    "@com_google_protobuf//:protobuf_java",
  ],
)

### Auth Service ###

java_library(
  name = "auth_service",
  srcs = glob(["src/main/java/com/wwttr/auth/*.java"]),
  deps = [
    "@com_google_protobuf//:protobuf_java",
    ":models",
    ":database_lib",
    ":api_lib",
  ],
)

java_test(
  name = "auth",
  test_class = "com.wwttr.auth.AuthServiceTest",
  srcs = glob(["src/test/java/com/wwttr/auth/*.java"]),
  deps = [
    ":auth_service",
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

### Game Service ###

java_library(
  name = "game_service",
  srcs = glob([
    "src/main/java/com/wwttr/game/*.java",
  ]),
  deps = [
    "@com_google_protobuf//:protobuf_java",
    ":api_lib",
    "auth_service",
    ":card_service",
    ":models",
    ":database_lib",
  ],
)

java_library(
  name = "models",
  srcs = glob([
    "src/main/java/com/wwttr/models/*.java",
    "src/main/java/com/wwttr/*/Api.java",  
  ]),
  deps = [
    ":api_lib",
    "@com_google_protobuf//:protobuf_java",
  ]
)

java_test(
  name = "game",
  test_class = "com.wwttr.game.GameServiceTest",
  srcs = glob([
    "src/test/java/com/wwttr/game/*.java",
    ]),
  deps = [
    ":game_service",
    ":auth_service",
    ":api_lib",
  ]
)

### Health Service ###

java_library(
  name = "health_service",
  srcs = glob(["src/main/java/com/wwttr/health/*.java"]),
  deps = [
    "@com_google_protobuf//:protobuf_java",
    ":models",
  ],
)
# java_test(
#   name = "health",
#   test_class = "com.wwttr.health.HealthTest",
#   srcs = glob(["src/test/java/com/wwttr/health/*.java"]),
#   deps = [
#     ":health_service",
#   ]
# )

### Card Service ###

java_library(
  name = "card_service",
  srcs = glob(["src/main/java/com/wwttr/card/*.java"]),
  deps = [
    ":models",
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
  ]
)

### Chat Service ###


java_library(
  name = "chat_service",
  srcs = glob(["src/main/java/com/wwttr/chat/*.java"]),
  deps = [
    ":models",
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

### Route Service ###

java_library(
  name = "route_service",
  srcs = glob(["src/main/java/com/wwttr/route/*.java"]),
  deps = [
    ":models",
    ":database_lib",
    ":api_lib",
    "@com_google_protobuf//:protobuf_java",
  ],
)

java_test(
  name = "route",
  test_class = "com.wwttr.route.RouteServiceTest",
  srcs = glob(["src/test/java/com/wwttr/route/*.java"]),
  deps = [
    "route_service"
  ]
)
