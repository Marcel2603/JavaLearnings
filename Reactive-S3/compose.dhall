let package =
      https://raw.githubusercontent.com/Marcel2603/DhallDockerCompose/master/package.dhall
        sha256:b3873be1a41883f89d24aa42e7fdff7f6261bee16fe05a21aa4c269978819c0e

let server_service =
      package.service::{
      , build = Some
          (package.text_or_build.Object package.build::{ context = "./server" })
      , container_name = Some "server"
      , ports = Some [ "9000:9000" ]
      , volumes = Some [ "server/target/server.jar:/opt/app.jar:ro" ]
      }

let client_service =
      package.service::{
      , build = Some
          (package.text_or_build.Object package.build::{ context = "./client" })
      , container_name = Some "client"
      , ports = Some [ "9001:9001" ]
      , volumes = Some [ "client/target/client.jar:/opt/app.jar:ro" ]
      }

let localstack_service =
      package.service::{
      , image = Some "localstack/localstack"
      , container_name = Some "localstack"
      , ports = Some [ "4566:4566" ]
      , restart = Some "unless-stopped"
      , environment = Some
          ( toMap
              { SERVICES = "s3", DEBUG = "1", DEFAULT_REGION = "eu-central-1" }
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
      , volumes = Some [ "/var/run/docker.sock:/var/run/docker.sock" ]
      }

in  package.compose::{
    , services = Some
        ( toMap
            { server = server_service
            , client = client_service
            , localstack = localstack_service
            }
        )
    }