data "aws_ami" "nat_ami" {
  most_recent = true

  filter {
    name   = "owner-alias"
    values = ["amazon"]
  }

  filter {
    name   = "name"
    values = ["amzn-ami-vpc-nat*"]
  }
}

resource "aws_instance" "nat" {
  ami           = "${data.aws_ami.nat_ami.id}"
  instance_type = "t2.micro"
  source_dest_check = false

  private_ip = "11.0.0.12"
  subnet_id  = "${aws_subnet.proxy_subnet_public.id}"

  tags {
    Name = "NAT_instance"
  }
}

# allocate a static IP
resource "aws_eip" "nat_eip" {
  vpc = true

  instance                  = "${aws_instance.nat.id}"
  associate_with_private_ip = "11.0.0.12"
  depends_on                = ["aws_internet_gateway.proxy_inet_gw"]
}
