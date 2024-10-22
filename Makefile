# Directory to search for .class files
SRC_DIR = src

BUILD_DIR = build
OUT_DIR = $(BUILD_DIR)/out
CLASSES_DIR = $(BUILD_DIR)/classes

# Output JAR file name
OUT_JAR_FILE = VCAndroidAuto.jar

# extracted LSD dir
LSD_SOURCE_DIR = lsd_java
LSD_JAR = lsd.jar
LSD_JXE = lsd.jxe

JXE2JAR_IMAGE = ghcr.io/adi961/jxe2jar:latest

# Classpath for javac
CLASSPATH = ".:$(LSD_JAR)"
# Javac options
JAVAC_OPTS = -source 1.2 -target 1.2 -cp $(CLASSPATH) -d $(CLASSES_DIR)

JDK= ./jdk
JAVAC = $(JDK)/bin/javac
JAR = $(JDK)/bin/jar

CFR_JAR = tools/cfr-0.152.jar
CFT_OPTS = $(LSD_JAR) --previewfeatures false --switchexpression false --removeinnerclasssynthetics false

# Default target
all: jar

# extract jdk
jdk:
	@unzip tools/ibm-java-ws-sdk-pxi3260sr4ifx.zip -d jdk

# Find all .java files (globally)
JAVA_FILES := $(shell find $(SRC_DIR) -name '*.java')

# Compile .java files
.PHONY: compile
compile:
	@echo "Compiling..."
	@mkdir -p $(CLASSES_DIR)
	@$(JAVAC) $(JAVAC_OPTS) $(JAVA_FILES)

# .class files into the jar
.PHONY: jar
jar: compile
	@echo "Packaging into $(OUT_DIR)/$(OUT_JAR_FILE)..."
	@mkdir -p $(OUT_DIR)
	@$(JAR) cvf $(OUT_DIR)/$(OUT_JAR_FILE) -C $(BUILD_DIR) .


# Clean the compiled files and jar
.PHONY: clean
clean:
	@echo "Cleaning up..."
	@rm -f $(OUT_JAR_FILE)
	@rm -rf $(BUILD_DIR)


ARGUMENT := $(wordlist 2,$(words $(MAKECMDGOALS)),$(MAKECMDGOALS))

.PHONY: extractOriginal
extractOriginal:
ifndef ARGUMENT
	$(error no class is defined. Usage: make extractOriginal com.example.MyClass)
endif
	@mkdir -p $(SRC_DIR)
	@java -jar $(CFR_JAR) $(CFT_OPTS) --jarfilter $(ARGUMENT) --outputdir $(SRC_DIR)
	$(eval TARGET_FILE := $(subst .,/, $(ARGUMENT)).java)
	@echo "Copying $(TARGET_FILE) to $(SRC_DIR)/$(TARGET_FILE)"
	@sed -i 's: final : /*final*/ :g' $(SRC_DIR)/$(TARGET_FILE)
	@sed -r -i 's:  @Override: // @Override:g' $(SRC_DIR)/$(TARGET_FILE)
	@perl -0777 -npi -e 's:    default (.*)\{\n    \}:    /*default*/ \1;:g' $(SRC_DIR)/$(TARGET_FILE)


.PHONY: lsd
lsd: lsd.jar lsd_java

lsd_java:
	@mkdir -p $(LSD_SOURCE_DIR)
	@java -jar $(CFR_JAR) $(CFT_OPTS) --outputdir $(LSD_SOURCE_DIR)

# Convert lsd.jxe to lsd.jar
lsd.jar:
	@docker run -it -v $(PWD):/data $(JXE2JAR_IMAGE) bash -c \
	 	"python2 JXE2JAR.py /data/$(LSD_JXE) /data/$(LSD_JAR) && chown $(shell id -u):$(shell id -g) /data/$(LSD_JAR)"

.PHONY: publishJxe2jar
publishJxe2jar: buildJxe2jar
	@docker push $(JXE2JAR_IMAGE)

.PHONY: buildJxe2jar
buildJxe2jar:
	@docker build -t $(JXE2JAR_IMAGE) .
