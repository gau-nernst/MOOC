#include <cmath>
#include <omp.h>
#include "image.h"
#include "stencil.h"

#define P png_byte

const int nTrials = 10;
const int skipTrials = 3; // Skip first iteration as warm-up

double Stats(double & x, double & dx) {
  x  /= (double)(nTrials - skipTrials);
  dx  = sqrt(dx/double(nTrials - skipTrials) - x*x);
}

int main(int argc, char** argv) {

  if(argc < 2) {
    printf("Usage: %s {file}\n", argv[0]);
    exit(1);
  }

  ImageClass<P> img_in (argv[1]);
  ImageClass<P> img_out(img_in.width, img_in.height);
  
  printf("\n\033[1mEdge detection with a 3x3 stencil\033[0m\n");
  printf("\nImage size: %d x %d\n\n", img_in.width, img_in.height);
  printf("\033[1m%5s %15s %15s %15s\033[0m\n", "Step", "Time, ms", "GB/s", "GFLOP/s"); fflush(stdout);

  double t, dt, g, dg, f, df;

  for (int iTrial = 1; iTrial <= nTrials; iTrial++) {
    const double t0 = omp_get_wtime();
    ApplyStencil(img_in, img_out);
    const double t1 = omp_get_wtime();

    const double ts   = t1-t0; // time in seconds
    const double tms  = ts*1.0e3; // time in milliseconds
    const double gbps = double(sizeof(P)*img_in.width*img_in.height*2)*1e-9/ts; // bandwidth in GB/s
    const double fpps = double(img_in.width*img_in.height*2*9)*1e-9/ts; // performance in GFLOP/s

    if (iTrial > skipTrials) { // Collect statistics
      t  += tms; 
      dt += tms*tms;
      g  += gbps;
      dg += gbps*gbps;
      f  += fpps;
      df += fpps*fpps;
    }

    // Output performance
    printf("%5d %15.3f %15.3f %15.3f %s\n", 
	   iTrial, tms, gbps, fpps, (iTrial<=skipTrials?"*":""));
    fflush(stdout);
  }

  Stats(t, dt);  
  Stats(g, dg);  
  Stats(f, df);  
  printf("-----------------------------------------------------\n");
  printf("\033[1m%s\033[0m\n%8s   \033[42m%8.1f+-%.1f\033[0m   \033[42m%8.1f+-%.1f\033[0m   \033[42m%8.1f+-%.1f\033[0m\n",
	 "Average performance:", "", t, dt, g, dg, f, df);
  printf("-----------------------------------------------------\n");
  printf("* - warm-up, not included in average\n\n");

  img_out.WriteToFile("output.png");
  printf("\nOutput written into output.png\n");

}
