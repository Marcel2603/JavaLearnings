let package =
      https://raw.githubusercontent.com/Marcel2603/DhallDockerCompose/master/package.dhall
        sha256:b3873be1a41883f89d24aa42e7fdff7f6261bee16fe05a21aa4c269978819c0e

let reactive_file_server_service =
      package.service::{
      , build = Some
          (package.text_or_build.Object package.build::{ context = "./ReactiveFileServer" })
      , container_name = Some "reactive-file-server"
      , environment = Some
          ( toMap
              { SPRING_PROFILES_ACTIVE = "compose"
              , LOCALSTACK_URL = "http://localstack:4566"
              , S3_BUCKET = "some-bucket"
              }
          )
      , ports = Some [ "9000:9000", "9010:9010" ]
      , volumes = Some [ "./ReactiveFileServer/target/server.jar:/opt/app.jar:ro" ]
      }

let reactive_client_service =
      package.service::{
      , build = Some
          (package.text_or_build.Object package.build::{ context = "./ReactiveClient" })
      , container_name = Some "reactive-client"
      , environment = Some
          ( toMap
              { SPRING_PROFILES_ACTIVE = "compose"
              , SERVER_URL = "http://reactive-file-server:9000"
              }
          )
      , ports = Some [ "9001:9001", "9011:9011" ]
      , volumes = Some [ "./ReactiveClient/target/client.jar:/opt/app.jar:ro" ]
      }

let localstack_service =
      package.service::{
      , image = Some "localstack/localstack"
      , container_name = Some "localstack"
      , ports = Some [ "4566:4566" ]
      , restart = Some "unless-stopped"
      , environment = Some
          ( toMap
              { SERVICES = "s3"
              , DEBUG = "1"
              , DEFAULT_REGION = "eu-central-1"
              , S3_BUCKET = "some-bucket"
              }
          )
      , networks = Some
          ( package.networks.Networks
              ( toMap
                  { default = package.network::{
                    , aliases = Some [ "test-bucket.localstack" ]
                    }
                  }
              )
          )
      , volumes = Some
        [ "/var/run/docker.sock:/var/run/docker.sock"
        , "./createS3.sh:/docker-entrypoint-initaws.d/createS3.sh"
        ]
      }

in  package.compose::{
    , services = Some
        ( toMap
            { reactive-server = reactive_file_server_service
            , reactive-client = reactive_client_service
            , localstack = localstack_service
            }
        )
    }
