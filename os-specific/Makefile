# This uses GNU Make syntax and extensions.

SANDBOX:=$(../../sandbox-not)
SUBDIRS:=Windows
_IN:=$(shell pwd)


debug:
	@echo $(_IN): MAKELEVEL is $(MAKELEVEL)
	@echo $(_IN): MAKEFLAGS is $(MAKEFLAGS)
	@echo $(_IN): _SANDBOX = \"$(_SANDBOX)\"
	@echo $(_IN): SUBDIRS = \"$(SUBDIRS)\"

install:
	touch $(SANDBOX)/eraseme

