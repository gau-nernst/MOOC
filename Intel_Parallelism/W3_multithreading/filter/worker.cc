#include <vector>
#include <algorithm>

//helper function , refer instructions on the lab page
void append_vec(std::vector<long> &v1, std::vector<long> &v2) {
  v1.insert(v1.end(),v2.begin(),v2.end());
}


void filter(const long n, const long m, float *data, const float threshold, std::vector<long> &result_row_ind) {
  
  // //work on one row at a time
  // for (long i = 0; i < n; i++){
  //   float sum = 0.0f;
  //   //compute sum of all the elements in a row
  //   for (long j = 0; j < m; j++) {
  //     sum += data[i*m + j];
  //   } 
    
  //   //store the sum in an array(vector) only if it is valid (i.e if it is greater than threshold
  //   if (sum > threshold) 
  //     result_row_ind.push_back(i);
  // }

#pragma omp parallel
  {
    std::vector<long> thread_result;

    #pragma omp for
      for (long i=0; i<n; i++) {
        float sum = 0.0f;
        for (long j=0; j<m; j++)
          sum += data[i*m+j];

        if (sum > threshold)
          thread_result.push_back(i);
      }

    #pragma omp critical
      append_vec(result_row_ind, thread_result);   
  }

  //sort the values stored in the vector
  std::sort(result_row_ind.begin(),
            result_row_ind.end());
}