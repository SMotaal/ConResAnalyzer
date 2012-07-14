figure('WindowStyle', 'docked');

imset = strcat( 'Approval 1200 - TV50', '/', 'G',  {'1', '3', '5', '7', '9', '11', '13'}, '.tif');

imscr   = imread('Approval 1200 - TV50/G11.tif');
imscrf  = real(fftshift(fft2(imscr)));
imflt   = im2bw(imscrf./max(imscrf(:)),0.05)~=1;

cells   = numel(imset);
rows    = floor(sqrt(cells));
columns = ceil(numel(imset)/rows);

for m = 1:cells
  im1   = imread(imset{m});
  im1f  = real(fftshift(fft2(im1)));
  
  im1fl = im1f.*imflt;
  
  im1ff = ifft2(ifftshift(im1fl));
  
  subplot(rows, columns, m);
  imshow(im1ff, []); % imshow([im1 im1ff./max(im1ff(:)).*255],[]);
end
