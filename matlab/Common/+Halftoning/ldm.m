function ldm = ldm(orig, dith, nfreq)

%LDM  Linear Distortion Measure
%	RATIO = LDM(IM1, IM2, MAXFREQ) computes the Linear distortion
%	ratio of IM2 with respect to IM1.
%	A weighting appropriate to the human visual system, as proposed
%	by Mannos and Sakrison (1) and modified by Mitsa and Varkur (2),
%	is used.  MAXFREQ specifies the spatial frequency in cycles per
% 	degree that corresponds to the Nyquist frequency in the x-direction.
%	If MAXFREQ is omitted, it defaults to 60 cyc/deg. 
%   A higher LDM implies more linear distortion.
%
%	Refs: (1) J. Mannos and D. Sakrison, "The effects of a visual
%	fidelity criterion on the encoding of images", IEEE Trans. Inf.
%	Theory, IT-20(4), pp. 525-535, July 1974.
%
%	(2) T. Mitsa and K. Varkur, "Evaluation of contrast sensitivity
%	functions for the formulation of quality measures incorporated
%	in halftoning algorithms", IEEE Int. Conference on Acoustics, Speech
%   and Signal Processing '93-V, pp. 301-304.
%   
%   (3) M. Valliappan, B. L. Evans, D. A. D. Tompkins, and F. Kossentini, 
%   ``Lossy Compression of Stochastic Halftones with JBIG2'', Proc. IEEE 
%    Int. Conf. on Image Processing, Oct. 25-28, 1999, vol. I, pp. 214-218, 
%    Kobe, Japan, invited paper. 
%
%	See also SNR, PSNR, TSNR, SNRVSF.
 
% June, July 2002

% Part of Halftoning Toolbox Version 1.2 released July 2005

% Copyright (c) 1999-2005 The University of Texas
% All Rights Reserved.
 
if nargin==2
  nfreq=60;
end

[x,y]=size(orig);
[xplane,yplane]=meshgrid(-x/2+0.5:x/2-0.5);	% generate mesh
plane=(xplane+i*yplane)/x*2*nfreq;
radfreq=abs(plane);				% radial frequency
 
% According to (2), we modify the radial frequency according to angle.
% w is a symmetry parameter that gives approx. 3 dB down along the
% diagonals.
 
w=0.7;
s=(1-w)/2*cos(4*angle(plane))+(1+w)/2;
radfreq=radfreq./s;

% Now generate the CSF

csf=2.6*(0.0192+0.114*radfreq).*exp(-(0.114*radfreq).^1.1);
f=find(radfreq<7.8909); csf(f)=0.9809+zeros(size(f));
 
% Find weighted SNR in frequency domain.  Note that, because we are not
% weighting the signal, we compute signal power in the spatial domain.
% This requires us to multiply by the image size in pixels to get the
% signal power in the frequency domain for division.

for k = 1:x,
  for j = 1:y,
     if(orig(k,j) == 0) 
          orig(k,j) = 1;
     end       
  end
end

norm_constant = (1/(x*y));

weight_num = fftshift(1 - (norm_constant * fft2(dith./orig))); % Do |1 - H(u,v)| 

orig_wt = fftshift(fft2(orig)).*csf;  %Compute Denominator
num_value = orig_wt.*weight_num;

mse=sum(sum(abs(orig_wt)));		% weighted error power
mss=sum(sum(abs(num_value)));  % signal power
ldm = mss/mse;


