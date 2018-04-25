provider "aws" {
  region = "us-west-2"
}

resource "aws_iam_role" "iam_for_lambda" {
  name = "iam_for_lambda"

  assume_role_policy = <<EOF
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Action": "sts:AssumeRole",
      "Principal": {
        "Service": "lambda.amazonaws.com"
      },
      "Effect": "Allow",
      "Sid": ""
    }
  ]
}
EOF
}

resource "aws_lambda_function" "runtime_the-proxy" {
  filename            = "scala/the-proxy/target/scala-2.12/the-proxy.jar"
  function_name       = "the-proxy"
  role                = "${aws_iam_role.iam_for_lambda.arn}"
  handler             = "com.summitcove.theproxy.MyHandler"
  source_code_hash    = "${base64sha256(file("scala/the-proxy/target/scala-2.12/the-proxy.jar"))}"
  runtime             = "java8"
  timeout             = "15"
  memory_size         = "512"

  environment {
    variables = {
      foo = "bar"
    }
  }

  # vpc_config {
  #   subnet_ids            = [ "${aws_subnet.proxy_subnet_private.id}" ]
  #   security_group_ids    = [ "${aws_default_security_group.default.id}" ]
  # }
}
