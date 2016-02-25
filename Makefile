# This uses GNU Make syntax and extensions.

MYAPP:=myservice
_IN:=$(shell pwd)
JARFILE:=../maven-build-artifacts/$(MYAPP)/$(MYAPP)-1.0-SNAPSHOT-jar-with-dependencies.jar
SANDBOX:=$(shell cd .. && pwd)/sandbox
SUBDIRS:=os-specific

.PHONY: usage
usage:
	@echo "Usage: make [{target}] [{variable}={value} ...]"
	@echo "Targets include:"
	@echo "  usage - [the default target] this message"
	@echo "  all - assemble and compile, but do not install."
	@echo "  install - installs under \$$SANDBOX"
	@echo "  clean - delete build artifacts."
	@echo "Variables include:"
	@echo "  SANDBOX - currently $(SANDBOX)"
	@echo "  JARFILE - the jar that gets installed as \$$SANDBOX/bin/$(_MYAPP).jar,"
	@echo "            currently $(JARFILE)"

# Don't use variables directly; they might not be set
override _SANDBOX := $(or $(SANDBOX),SANDBOX)
export _SANDBOX
override _MYAPP   := $(or $(MYAPP),MYAPP)
override _JARFILE := $(or $(JARFILE),JARFILE)

debug: 
	@echo $(_IN): MAKELEVEL is $(MAKELEVEL)
	@echo $(_IN): MAKEFLAGS is $(MAKEFLAGS)
	@echo $(_IN): _SANDBOX = \"$(_SANDBOX)\"
	@echo $(_IN): _MYAPP = \"$(_MYAPP)\"
	@echo $(_IN): _JARFILE = \"$(_JARFILE)\"
	@echo $(_IN): SUBDIRS = \"$(SUBDIRS)\"
	@for s in $(SUBDIRS); do \
	  [ -d "$$s" ] && $(MAKE) -C $$s debug; \
	done;

.PHONY: all
all: $(_JARFILE)

.PHONY: FORCE
$(JARFILE): FORCE
	mvn package

.PHONY: clean
clean:
	mvn clean

.PHONY: install
install: $(_SANDBOX)/bin $(_SANDBOX)/bin/$(_MYAPP) $(_SANDBOX)/bin/$(_MYAPP).jar install-subdirs

.PHONE: install-subdirs
install-subdirs:
	@for s in $(SUBDIRS); do \
	  [ -d "$$s" ] && $(MAKE) -C $$s install; \
	done;

$(_SANDBOX)/bin:
	[ -d "$@" ] || mkdir -p $@

$(_SANDBOX)/bin/$(_MYAPP): $(_MYAPP)
	cp $< $@

$(_SANDBOX)/bin/$(_MYAPP).jar: $(_JARFILE)
	cp $< $@
