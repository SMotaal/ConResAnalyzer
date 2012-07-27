classdef UserFunction < Grasppe.ConRes.PatchGenerator.Processors.ImageProcessor
  %PATCHGENERATOR Summary of this class goes here
  %   Detailed explanation goes here
  
  properties
    ImageIndex = 0;
  end
  
  methods
    function output = Run(obj)
      output  = obj.Input;
      variables = obj.Variables;
      params  = obj.Parameters;
      
      try
        obj.sandbox(params, output);
      catch err
        disp('Failed to execute function');
        disp(err);
      end
      
      obj.Variables = variables;
    end
    
    function sandbox(obj, params, output)
      fourier   = @(varargin) obj.fourier(varargin{:});
      forwardFFT   = @(varargin) obj.forwardFFT(varargin{:});
      inverseFFT   = @(varargin) obj.inverseFFT(varargin{:});
      display   = @(varargin) obj.display(varargin{:});
      
      processData = output.ProcessData;
      %resolution = @
      
      % fx = varargin{1};
      
      structVars(obj.Variables);
      
      structVars(params);
      
      s = processData.getDataStruct();
      
      % Patch   = s.Patch1;
      % Screen  = s.Screen1;
      % Print   = s.Screen1.PrintParameters;
      % Scan    = s.Scan1;
      
      structVars(s);
      clear s;
      
      image = output.Image;
      
      try
        image = eval(findField(params, 'expression'));
      catch err
        try
          eval(findField(params, 'expression'));
        end
      end
      
      output.Image = image;
      
      return;
    end
    
    function image = display(obj, varargin)
      output  = evalin('caller', 'output');
      image   = evalin('caller', 'image');
      source  = varargin{1};
      
      switch source
        case {0, 'Output'}
          % figure, imshow(image);
          obj.saveImage(image);
        case {1, 'Image'}
        case {2, 'Fourier'}
          switch (output.Domain)
            case 'frequency'
            otherwise
              image = obj.forwardFFT(image);
          end
          % figure, imshow(image);
          % image     = real(log(1+abs(image)));
          % imageMin  = min(image(:)); imageMax = max(image(:));
          % image     = (image-imageMin) / (imageMax-imageMin);
          obj.saveImage(image);
        case {3, 'Variables'}
          disp(obj.Variables);
      end
    end
    
    function saveImage(obj, image)
      obj.ImageIndex = obj.ImageIndex + 1;
      imwrite(image, ['Output/image' int2str(obj.ImageIndex) '.png']);
    end
    
    function image = forwardFFT(obj, image)
      % Sizing & Padding
      sP  = size(image,1);
      sQ  = size(image,2);
      nP  = 1-mod(sP,2);
      nQ  = 1-mod(sQ,2);
      fP  = ceil(2*(sP-nP));
      fQ  = ceil(2*(sQ-nQ));
      
      image = image(1:end-(nP),1:end-(nQ));
      
      image = fftshift(fft2(image, fP, fQ));
    end
    
    function image = inverseFFT(obj, image)
      % Unpadding
      sP  = size(image,1);
      sQ  = size(image,2);
      fP  = ceil(sP/2);
      fQ  = ceil(sQ/2);
      image = ifft2(ifftshift(image)); %, fP, fQ);
      image = image(1:fP, 1:fQ);
    end
    
    function image = fourier(obj, varargin)
      output  = evalin('caller', 'output');
      image   = evalin('caller', 'image');
      
      method  = 1;
      
      
      switch (output.Domain)
        case 'frequency'
          image = obj.inverseFFT(image);
          output.Domain = 'spatial';
          
        otherwise % case 'spatial'
          
          image = obj.forwardFFT(image);
          output.Domain = 'frequency';
      end
      
      disp([method varargin{1}]);
    end
  end
end
