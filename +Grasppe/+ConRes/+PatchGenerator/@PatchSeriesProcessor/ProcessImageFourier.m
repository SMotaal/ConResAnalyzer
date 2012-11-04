function [FFT SRF FIMG IMG]  = ProcessImageFourier(img, bandParameters)
  
  import(eval(NS.CLASS));
  
  nout = nargout;
  out1 = nout > 0;  out2 = nout > 1; 	out3 = nout > 2; 	out4 = nout > 3;
  
  try
    
    if ~exist('bandParameters', 'var'), bandParameters = {[], []}; end
    if ~iscell(img), img = {img}; end
    
    
    IMG = cell(size(img));
    FFT = IMG;  SRF = IMG;  FIMG = IMG;
    
    for m = 1:numel(img)
      
      if ischar(img{m}), IMG{m} = PatchSeriesProcessor.LoadImage(img{m});
      else IMG{m}               = img{m}; end
            
      if isreal(IMG{m})
        if m==2
          FFT{m} = forwardFFT(histeq(IMG{m}));
        else
          FFT{m} = forwardFFT(IMG{m});
        end
      else FFT{m}               = IMG{m}; end
      
      if out2, SRF{m}           = Grasppe.Kit.ConRes.CalculateBandIntensity(FFT{m}, 75, bandParameters{2}); end
      if out3, FIMG{m}          = realImage(FFT{m}); end
    end
    
    
    if ~out4, clear IMG;  end
    if ~out3, clear FIMG; end
    if ~out2, clear SRF;  end
    if ~out1, clear FFT;  end
    
  catch err
    debugStamp(err, 1);
    rethrow(err);
  end
  
end

function img = forwardFFT(img)
  sP  = size(img,1);
  sQ  = size(img,2);
  nP  = 1-mod(sP,2);
  nQ  = 1-mod(sQ,2);
  fP  = ceil(2*(sP-nP));
  fQ  = ceil(2*(sQ-nQ));
  
  img = img(1:end-(nP),1:end-(nQ));
  img = fftshift(fft2(img, fP, fQ));
  
end


function img = realImage(img)
  if ~isreal(img)
    img       = real(log(1+abs(img)));
    fftMin    = min(img(:)); imageMax = max(img(:));
    img       = (img-fftMin) / (imageMax-fftMin);
  end
end
