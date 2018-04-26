scaffold:
ifeq ($(NAME),)
	exit 1
endif
ifeq ($(ORG),)
	exit 2
endif
ifneq (,$(wildcard scala/$(NAME)))
	@echo project already exists
	exit 3
endif
	cd scala;\
	sbt new yeghishe/scala-aws-lambda-seed.g8 --name=${NAME} --organization=${ORG}
	sed 's/coverageEnabled := true/coverageEnabled := false/' scala/${NAME}/build.sbt
	source scaffold.sh;\
	scaffold_lambda_resources ${NAME} ${ORG}

deploy:
ifeq ($(APP),)
	exit 1
endif
	cd scala/${APP};\
	sbt clean assembly
	# todo coverage check
	# todo style warning
	terraform apply
