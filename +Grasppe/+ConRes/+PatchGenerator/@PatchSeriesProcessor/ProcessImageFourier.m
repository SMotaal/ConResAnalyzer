function [FFT SRF FIMG IMG]  = ProcessImageFourier(imagePaths, bandParameters)
  
  import(eval(NS.CLASS));
  
  try
    
    if ~exist('bandParameters', 'var'), bandParameters = {[], []}; end
    if ~iscell(imagePaths), imagePaths = {imagePaths}; end
    
    IMG           = cell(size(imagePaths));
    FFT           = IMG;
    SRF           = IMG;
    FIMG          = IMG;
    
    for m = 1:numel(imagePaths)
      
      IMG{m}      = PatchSeriesProcessor.LoadImage(imagePaths{m}); %'screen', screenID);
      FFT{m}      = forwardFFT(IMG{m});
      if nargout > 1
        SRF{m}    = Grasppe.Kit.ConRes.CalculateBandIntensity(FFT{m}, bandParameters{:});
      end
      if nargout > 2
        FIMG{m}   = realImage(FFT{m});
      end
    end
    
    if nargout < 4, clear IMG;  end
    if nargout < 3, clear FIMG; end
    if nargout < 2, clear SRF;  end
    if nargout < 1, clear FFT;  end
        
  catch err
    debugStamp(err, 1);
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
