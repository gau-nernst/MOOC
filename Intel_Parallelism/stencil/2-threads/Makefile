CXX=icpc
CXXFLAGS=-c -qopenmp -qopt-report=5 -xMIC-AVX512
LDFLAGS=-qopenmp -lpng

OBJECTS=main.o image.o stencil.o

stencil: $(OBJECTS)
	$(CXX) $(LDFLAGS) -o stencil $(OBJECTS)

all:	stencil

run:	all
	./stencil test-image.png

queue:	all
	echo 'cd $$PBS_O_WORKDIR ; ./stencil test-image.png' | qsub -l nodes=1:flat -N edgedetection

clean:
	rm -f *.optrpt *.o stencil *output.png *~ edgedetection.*
