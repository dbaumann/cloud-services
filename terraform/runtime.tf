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
      "Sid": "AssumeRole",
      "Effect": "Allow",
      "Action": "sts:AssumeRole",
      "Principal": {
        "Service": [
          "lambda.amazonaws.com"
        ]
      }
    }
  ]
}
EOF
}

resource "aws_iam_role_policy" "lambda_role_policy" {
  name   = "lambda_role_policy"
  role   = "${aws_iam_role.iam_for_lambda.id}"
  policy = <<EOF
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Sid": "Logs",
      "Effect": "Allow",
      "Action": [
        "logs:CreateLogGroup",
        "logs:CreateLogStream",
        "logs:PutLogEvents"
      ],
      "Resource": "arn:aws:logs:*:*:*"
    },
    {
      "Sid": "VPCAccess",
      "Effect": "Allow",
      "Action": [
        "ec2:CreateNetworkInterface",
        "ec2:DescribeNetworkInterfaces",
        "ec2:DetachNetworkInterface",
        "ec2:ModifyNetworkInterfaceAttribute",
        "ec2:DeleteNetworkInterface"
      ],
      "Resource": "*"
    }
  ]
}
EOF
}

resource "aws_lambda_function" "runtime_the-proxy" {
  filename            = "../scala/the-proxy/target/scala-2.12/the-proxy.jar"
  function_name       = "the-proxy"
  role                = "${aws_iam_role.iam_for_lambda.arn}"
  handler             = "org.dbaumann.theproxy.ProxyHandler"
  source_code_hash    = "${base64sha256(file("../scala/the-proxy/target/scala-2.12/the-proxy.jar"))}"
  runtime             = "java8"
  timeout             = "15"
  memory_size         = "256"

  lifecycle {
    ignore_changes = ["environment"]
  }

  vpc_config {
    subnet_ids            = [ "${aws_subnet.proxy_subnet_private.id}" ]
    security_group_ids    = [ "${aws_default_security_group.default.id}" ]
  }
}
