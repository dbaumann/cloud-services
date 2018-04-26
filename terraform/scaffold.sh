function scaffold_lambda_resources {
  local NAME=${1}
  local PACKAGE=${2}.${1//[-_]/}
tee -a runtime.tf <<EOF

resource "aws_lambda_function" "runtime_$NAME" {
  filename            = "scala/$NAME/target/scala-2.12/$NAME.jar"
  function_name       = "$NAME"
  role                = "\${aws_iam_role.iam_for_lambda.arn}"
  handler             = "$PACKAGE.MyHandler"
  source_code_hash    = "\${base64sha256(file("scala/$NAME/target/scala-2.12/$NAME.jar"))}"
  runtime             = "java8"
  timeout             = "15"
  memory_size         = "512"
}
EOF
}
