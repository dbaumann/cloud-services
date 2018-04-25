# # allocate a virtual private cloud
# resource "aws_vpc" "proxy_vpc" {
#     cidr_block = "11.0.0.0/16"
#     tags {
#         Name = "proxy-vpc"
#     }
# }

# # create a private subnet in which the runtime will operate
# resource "aws_subnet" "proxy_subnet_private" {
#     cidr_block = "11.0.1.0/24"
#     vpc_id = "${aws_vpc.proxy_vpc.id}"
#     tags {
#         Name = "proxy-vpc-subnet-private"
#     }
# }

# # create a public subnet from which traffic will appear to orignate
# resource "aws_subnet" "proxy_subnet_public" {
#     cidr_block = "11.0.0.0/24"
#     vpc_id = "${aws_vpc.proxy_vpc.id}"
#     tags {
#         Name = "proxy-vpc-subnet-public"
#     }
# }


# # -- route all traffic from private subnet to public subnet

# resource "aws_route_table" "proxy_routes_private" {
#     vpc_id = "${aws_vpc.proxy_vpc.id}"

#     route {
#         cidr_block = "0.0.0.0/0"
#         nat_gateway_id = "${aws_nat_gateway.proxy_nat_gw.id}"
#     }
# }

# resource "aws_route_table_association" "proxy_routes_private" {
#     route_table_id = "${aws_route_table.proxy_routes_private.id}"
#     subnet_id = "${aws_subnet.proxy_subnet_private.id}"
# }

# resource "aws_eip" "proxy_eip" {
#     # allocate a static ip
#     vpc = true
# }

# resource "aws_nat_gateway" "proxy_nat_gw" {
#     allocation_id     = "${aws_eip.proxy_eip.id}"
#     subnet_id         = "${aws_subnet.proxy_subnet_public.id}"
# }


# # -- route all traffic from public subnet to the internet

# resource "aws_internet_gateway" "proxy_inet_gw" {
#     vpc_id = "${aws_vpc.proxy_vpc.id}"
#     tags {
#         Name = "proxy-vpc-inet-gw"
#     }
# }

# resource "aws_route_table" "proxy_routes_public" {
#     vpc_id = "${aws_vpc.proxy_vpc.id}"
#     route {
#         cidr_block = "0.0.0.0/0"
#         gateway_id = "${aws_internet_gateway.proxy_inet_gw.id}"
#     }
# }

# resource "aws_route_table_association" "proxy_routes_public" {
#     route_table_id    = "${aws_route_table.proxy_routes_public.id}"
#     subnet_id         = "${aws_subnet.proxy_subnet_public.id}"
# }