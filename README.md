# DeskLights128.visualizer
DeskLights128 Visualizer program using Spectrum Lab

Uses Spectrum Lab for FFT data: http://www.qsl.net/dl4yhf/speclab/install_speclab.zip ( http://www.qsl.net/dl4yhf/spectra1.html )


Configure options in Spectrum Lab:
Options
  FFT settings
    length = 32 (16 'real' outputs, the number of columns we have to display on)
  Memory
    Max. allowed size = 256 (64 by default, seems a bit low/laggy?)
  Confugire and supervise HTTP Server
    enable HTTP Server ( check with this URL: http://127.0.0.1/_fft_expt.json )
