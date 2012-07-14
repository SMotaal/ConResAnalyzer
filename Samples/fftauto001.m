figure('WindowStyle', 'docked');

im1   = imread('B11.tif');  % imread('conresA2_158.tif');
imscr = imread('G13.tif');  % imread('conresA2_164.tif');

subplot(2,3,1), imshow(im1); subplot(2,3,2), imshow(imscr);


im1f = real(fftshift(fft2(im1)));
imscrf = real(fftshift(fft2(imscr)));

subplot(2,3,4), imshow(log(im1f), []); subplot(2,3,5), imshow(log(imscrf), []);

imflt = im2bw(imscrf./max(imscrf(:)),0.02)~=1;

subplot(2,3,6), imshow(imflt, []);
subplot(2,3,3), imshow(ifft2(ifftshift(im1f.*imflt)));
