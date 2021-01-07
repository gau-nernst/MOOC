CXX=mpiicpc
CXXFLAGS=-c -qopenmp -qopt-report=5 -xMIC-AVX512
LDFLAGS=-qopenmp -lpng -lmemkind

OBJECTS=main.o image.o stencil.o

stencil: $(OBJECTS)
	$(CXX) $(LDFLAGS) -o stencil $(OBJECTS)

all:	stencil

run:	all
	./stencil test-image.png

queue:	all
	echo 'cd $$PBS_O_WORKDIR ; mpirun -machinefile $$PBS_NODEFILE ./stencil test-image.png' | qsub -l nodes=4:flat -N edgedetection

clean:
	rm -f *.optrpt *.o stencil *output.png *~ edgedetection.*
