resource "aws_iam_user" "zapier" {
  name = "zapier"
}

resource "aws_iam_user_policy" "zapier_invoke" {
  name = "zapier_invoke"
  user = "${aws_iam_user.zapier.name}"

  policy = <<EOF
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Sid": "ZapierList",
            "Effect": "Allow",
            "Action": [
                "lambda:ListFunctions"
            ],
            "Resource": "*"
        },
        {
            "Sid": "ZapierInvoke",
            "Effect": "Allow",
            "Action": [
                "lambda:InvokeFunction",
                "lambda:GetFunction"
            ],
            "Resource": "${aws_lambda_function.runtime_the-proxy.arn}"
        }
    ]
} 
EOF
}
