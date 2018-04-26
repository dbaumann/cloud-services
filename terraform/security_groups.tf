resource "aws_default_security_group" "default" {
  vpc_id = "${aws_vpc.proxy_vpc.id}"

  ingress {
    self        = true
    protocol    = "-1"
    from_port   = 0
    to_port     = 0
    description = "Allows all inbound traffic from other instances associated with the default security group"
  }

  egress {
    cidr_blocks = ["0.0.0.0/0"]
    protocol    = "-1"
    from_port   = 0
    to_port     = 0
    description = "Allows all outbound traffic from the instance"
  }
}

resource "aws_security_group" "nat_sg" {
  name        = "nat_sg"
  description = "Security group for nat instances which allows HTTP/S traffic to internet"
  vpc_id = "${aws_vpc.proxy_vpc.id}"

  ingress {
    cidr_blocks = ["11.0.1.0/24"]
    protocol    = "tcp"
    from_port   = 0
    to_port     = 80
    description = "Allow inbound HTTP traffic from servers in the private subnet"
  }

  ingress {
    cidr_blocks = ["11.0.1.0/24"]
    protocol    = "tcp"
    from_port   = 0
    to_port     = 443
    description = "Allow inbound HTTPS traffic from servers in the private subnet"
  }

  ingress {
    cidr_blocks = ["11.0.1.0/24"]
    protocol    = "tcp"
    from_port   = 0
    to_port     = 22
    description = "Allow inbound SSH access to the NAT instance from servers in the private subnet"
  }

  egress {
    cidr_blocks = ["0.0.0.0/0"]
    protocol    = "tcp"
    from_port   = 0
    to_port     = 80
    description = "Allow outbound HTTP access to the Internet"
  }

  egress {
    cidr_blocks = ["0.0.0.0/0"]
    protocol    = "tcp"
    from_port   = 0
    to_port     = 443
    description = "Allow outbound HTTPS access to the Internet"
  }

  tags {
    Name = "allow_http"
  }
}