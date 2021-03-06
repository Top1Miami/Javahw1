SOURCES = $(shell find src -type f -name "*.java")
CLASSES = $(patsubst src/%.java,out/%.class,$(SOURCES))
MAINCLASS = formulaIIV/Formula

all: $(CLASSES)

run:
	java -classpath out ${MAINCLASS}

pack:
	zip Jawahw1-master.zip -r Makefile src

clean:
	rm -rf out

$(CLASSES): $(SOURCES) out
	javac $(SOURCES) -d out

out:
	mkdir -p out