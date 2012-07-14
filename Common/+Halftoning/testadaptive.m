
% Script to Test Adaptive Threshold Modulation Error Diffusion Halftoning schemes
% Sample Test file for exploring options with GREENNOISE
% and Edge Enhancement Halftoning
% See Also GREEN_ADAP, GREEN_FIXED, EDGERRDIFF_L, EDGERRDIFF_L_H

% Part of Halftoning Toolbox Version 1.2 released July 2005

% Copyright (c) 1999-2005 The University of Texas
% All Rights Reserved.

%set halftoning parameters
l=0;
v=1;
dbf=0;
nu1=0.0025;
nu2 =0.005;
nu3 = 0.01;
dir = 1;

%Open File for writing results
output = fopen('results.txt','w');

%%Load the lena image
lena = imread('lenna.tif');
figure(1);
imshow(lena);
lna = double(lena);
%lna = read_img('Lenna.raw',256);
%show_img(lna);

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Optimum Edge Enhancement halftoning (Adapt L) -- Error Filter set to Floyd-Steinberg%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
fc = [1 5 3; 7 (-99*16) 0]/16;
[lna_f,qn,lna_L_f] = errdiffa(lna,fc,l,dir,v,dbf,nu1);
lna_floyd_adap = uint8(((lna_f + 1)/2)*255);
%lna_floyd_adap = ((lna_f + 1)/2)*255;
%[lna_f,qn,lna_L_f2] = errdiffa(lna,fc,l,dir,v,dbf,nu2);
%[lna_f,qn,lna_L_f3] = errdiffa(lna,fc,l,dir,v,dbf,nu3);
figure(2);
imshow(lna_floyd_adap);
fprintf(output,'%6.2f  %12.8f\n',lna_L_f);

%show_img(lna_floyd_adap);

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%Optimum Edge Enhancement halftoning (Adapt L) --- Error Filter set to Jarvis%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
fc = [1 3 5 3 1;3 5 7 5 3;5 7 -(99*48) 0 0] /48;
[lna_j,qn,lna_L_j] = errdiffa(lna,fc,l,dir,v,dbf,nu2);
lna_jarvis_adap = uint8(((lna_j + 1)/2)*255);
%[mix_j,qn,mix_L_j] = errdiffa(mix,fc,l,dir,v,dbf,nu2);
%lna_jarvis_adap = ((lna_j + 1)/2)*255;
figure(3);
imshow(lna_jarvis_adap);
fprintf(output,'%6.2f  %12.8f\n',lna_L_j);
%show_img(lna_jarvis_adap);


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%Optimum Edge Enhancement halftoning (Adapt L and H - Error Filter Initial Guess FS)%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
fc = [1 5 3; 7 (-99*16) 0]/16;
[lna_filt_f,qn,L_filter_fl,filt_init_FS,u_init_FS,K_init_FS] = errdiffN(lna,fc,l,dir,v,dbf);
lna_floyd_adap_filt = uint8(((lna_filt_f + 1)/2)*255);
figure(4);
imshow(lna_floyd_adap_filt);
fprintf(output,'%6.2f  %12.8f\n',L_filter_fl);
fprintf(output,'%6.2f  %12.8f\n',filt_init_FS);
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%Optimum Edge Enhancement halftoning (Adapt L and H - Error Filter Initial Guess Jarvis)%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
fc = [1 5 3; 7 (-99*16) 0]/16;
[lna_filt_j,qn,L_filter_j,filt_init_j,u_init_j,K_init_j] = errdiffN(lna,fc,l,dir,v,dbf);
lna_j_adap_filt = uint8(((lna_filt_j + 1)/2)*255);
figure(5);
imshow(lna_j_adap_filt);
fprintf(output,'%6.2f  %12.8f\n',L_filter_j);
fprintf(output,'%6.2f  %12.8f\n',filt_init_j);

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%Optimum Green Noise halftoning -- Set G = 0.5, Error filter to Stucki Adapt Hysteresis Filter Coefficients Initial Guess FS%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
Fs = [1 5 3; 7 (-99*16) 0]/16;  % Fs initial Guess
NSt = [0 .5 0;.5 0 0];  % Non-Standard Weights
G = 0.5;     % Hysteresis Constant G controls the size of dot clusters
out_green_fixed = green(lna,Fs,NSt,G,dir,v);
[out_green_adap,b,c1,c2,c3,c4] = green_di(lna,Fs,NSt,G,dir,v);
lna_floyd_green = uint8((out_green_adap*255));
figure(6);
imshow(lna_floyd_green);
fprintf(output,'%6.2f  %12.8f\n',b);
fprintf(output,'%6.2f  %12.8f\n',c1);
fprintf(output,'%6.2f  %12.8f\n',c2);
fprintf(output,'%6.2f  %12.8f\n',c3);
fprintf(output,'%6.2f  %12.8f\n',c4);


%%%%%%%%%%%%%%%%%%%%%%%%%  Show Results as log |error| %%%%%%%%%%%%%%%%%%%%%%%%%%%%

original = lna/255;
err_fixed = original - out_green_fixed;
err_adap = original - out_green_adap;

[tfer_green_fixed,tfer_green_adap,rads] = disp_err(err_fixed,err_adap,300);
grid on;




fclose(output);












