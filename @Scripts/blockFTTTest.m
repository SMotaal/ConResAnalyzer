dataColumn = 4;

retinaValue = 15;

retina = @(x) gaussian(x, retinaValue*3/2, retinaValue/2);

figure

htimg = imresize(retina(Scripts.fftPlotBlock(data, 'Halftone')), 0.25, 'bicubic');
htfft = fftshift(fft2(htimg, size(htimg,1)*2, size(htimg,2)*2));
[htfq htdata] = Grasppe.Kit.ConRes.CalculateBandIntensity(htfft);
subplot(2,3,[1:3]), hold on, ...
  plot(log(htdata(2:end, dataColumn)), 'Color', 'red');
subplot(2,3,4), imshow(htimg);

ctimg = imresize(retina(Scripts.fftPlotBlock(data, 'Contone')), 0.25,  'bicubic');
ctfft = fftshift(fft2(ctimg, size(ctimg,1)*2, size(ctimg,2)*2));
[ctfq ctdata] = Grasppe.Kit.ConRes.CalculateBandIntensity(ctfft);
subplot(2,3,[1:3]), hold on, ...
  plot(log(ctdata(2:end, dataColumn)), 'Color', 'green');
subplot(2,3,5), imshow(ctimg);

mtimg = imresize(retina(Scripts.fftPlotBlock(data, 'Monotone')), 0.25, 'bicubic');
mtfft = fftshift(fft2(mtimg, size(mtimg,1)*2, size(mtimg,2)*2));
[mtfq mtdata] = Grasppe.Kit.ConRes.CalculateBandIntensity(mtfft);
subplot(2,3,[1:3]), hold on, ...
  plot(log(mtdata(2:end, dataColumn)), 'Color', 'blue');
subplot(2,3,6), imshow(mtimg);
