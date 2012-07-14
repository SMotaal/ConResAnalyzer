% Part of Halftoning Toolbox Version 1.2 released July 2005

% Copyright (c) 1999-2005 The University of Texas
% All Rights Reserved.
%All 4 for Peppers

l = [1 1 1;1 1 1;1 1 1];
IM = imread('lenacolor.tif');
IMred = IM(:,:,1);
IMgreen = IM(:,:,2);
IMblue = IM(:,:,3);
figure;
imshow(IMred,IMgreen, IMblue);
in_dac = double(imread('lenacolor.tif'));

[m,n,dummy] = size(in_dac);
in = crt_char(in_dac);

%[F OF] = runVecFiltOpt4(7); % YUV

Fflohr  = [0.5269   -0.0000    0.0700   -0.0000   -0.0000    0.0068    0.0000    0.0668 0.0000    0.2814    0.0000    0.0481; 
      0.0000    0.4577   -0.0000   -0.0000   -0.0000   -0.0000    0.0930    0.2328 0.0000   -0.0000    0.1938    0.0228;
     -0.0000   -0.0000    0.0000   -0.0000   -0.0000    0.0000    0.0330   -0.0000 0.9670   -0.0000   -0.0000   -0.0000];

%Matrix Valued Vector Error Filter

%[out_fs,qn_fs,K_fs] = vecdiff(in); %fs halftone
%clear qn_fs,K_fs;
[out_vec,qn_vec,K_vec] = vecdiff(in,1,l,0,Fflohr); %vector halftone 
%res_corr(in/255,qn_vec)
imred = out_vec(:,:,1);
imgreen = out_vec(:,:,2);
imblue = out_vec(:,:,3);
figure;
imshow(imred, imgreen, imblue);
%imwrite(uint8(255*scale_co(out_vec)),'vec_256lena.tiff','tiff'); %write vector halftone
%imwrite(uint8(255*scale_co(out_fs)),'fs_256lena.tiff','tiff'); %write fs halftone
%imwrite(uint8(255*scale_co(qn_vec)),'vec_qn_256lena.tiff','tiff'); %write error image for vec halftone
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

%Fopp  = [0.5580    0.0000    0.0520   -0.0000   -0.0000   -0.0000    0.0000   -0.0000 -0.0000    0.3436   -0.0000    0.0464; 
 %     -0.0000    0.4799   -0.0000   -0.0000   -0.0000    0.0000    0.0486    0.2477 0.0000   -0.0000    0.2238    0.0000;
  %   0.0000   -0.0000    0.2756   -0.0000    0.0000    0.0000    0.0653   -0.0000 0.6591    0.0000   -0.0000    0.0000];
 
%  FYUV  = [0.5657    0.0000    0.0000    0.0000   -0.0000    0.0009    0.0307   -0.0000 0.0000    0.3873    0.0154    0.0000; 
 %      -0.0000    0.4818    0.0000    0.0008    0.0000   -0.0000    0.0012    0.2769 0.0000   -0.0000    0.2394   -0.0000;
 %     -0.0000   -0.0000    0.4956    0.0014   -0.0000    0.0000   -0.0000    0.0043 0.2592    0.0000    0.0000    0.2395];
 
 
% FYIQ  = [0.6005    0.0000    0.0000    0.0000    0.0000   -0.0000    0.0000   -0.0000 0.0000    0.3995   -0.0000   -0.0000; 
%      -0.0000    0.4659    0.0212   -0.0000    0.0000    0.0000    0.0297    0.2542  0.0000   -0.0000    0.2039    0.0251;
%     0.0000   -0.0000    0.5424    0.0000   -0.0000   -0.0000   -0.0000    0.0203 0.1278   -0.0000    0.0000    0.3095];