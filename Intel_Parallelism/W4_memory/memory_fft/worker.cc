#include <mkl.h>
#include <hbwmalloc.h>

//implement scratch buffer on HBM and compute FFTs, refer instructions on Lab page
void runFFTs( const size_t fft_size, const size_t num_fft, MKL_Complex8 *data, DFTI_DESCRIPTOR_HANDLE *fftHandle) {
  // for(size_t i = 0; i < num_fft; i++) {
  //   DftiComputeForward (*fftHandle, &data[i*fft_size]);
  // }
  
  MKL_Complex8 *buff;
  hbw_posix_memalign((void**) &buff, 4096, sizeof(MKL_Complex8)*fft_size);

  for(size_t i=0; i<num_fft; i++) {
#pragma omp parallel for
    for(size_t j=0; j<fft_size; j++) {
      buff[j].real = data[i*fft_size+j].real;
      buff[j].imag = data[i*fft_size+j].imag;
    }

    DftiComputeForward(*fftHandle, buff);
    
#pragma omp parallel for
    for(size_t j=0; j<fft_size; j++) {
      data[i*fft_size+j].real = buff[j].real;
      data[i*fft_size+j].imag = buff[j].imag;
    }
  }

  hbw_free(buff);
}