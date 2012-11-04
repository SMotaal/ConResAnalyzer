classdef UserFunction < Grasppe.ConRes.PatchGenerator.Processors.ImageProcessor
  %PATCHGENERATOR Summary of this class goes here
  %   Detailed explanation goes here
  
  properties(Transient)
    ImageIndex = 0;
  end
  
  methods
    function output = Run(obj)
      output  = obj.Input;
      variables = obj.Variables;
      params  = obj.Parameters;
      
      output.Variables = variables;
      try
        obj.sandbox(params, output);
      catch err
        disp('Failed to execute function');
        debugStamp(err,1);
      end
      
      obj.Variables = output.Variables;
    end
    
    function sandbox(obj, params, output)
      
      import Grasppe.ConRes.FX;
      
      %debugging = true;
      
      try
        fourier       = @(varargin) obj.fourier(varargin{:});
        forwardFFT    = @(varargin) obj.forwardFFT(varargin{:});
        inverseFFT    = @(varargin) obj.inverseFFT(varargin{:});
        
        fFFT          = @(varargin) forwardFFT(varargin{:});
        iFFT          = @(varargin) inverseFFT(varargin{:});
        
        display       = @(varargin) obj.display(varargin{:});
        
        add           = @(varargin) obj.algebra('add', varargin{:});
        subtract      = @(varargin) obj.algebra('subtract', varargin{:});
        multiply      = @(varargin) obj.algebra('multiply', varargin{:});
        divide        = @(varargin) obj.algebra('divide', varargin{:});
        
        sub           = @(varargin) subtract(varargin{:});
        mul           = @(varargin) multiply(varargin{:});
        div           = @(varargin) divide(varargin{:});
        
        normalize     = @(x)        x/nanmax(x(:));
        invert        = @(x)        1-im2double(x);
        binarize      = @(x,y)      im2bw(real(x),y);
        
        bin           = @(x,y)      binarize(x, y);
        nor           = @(x)        normalize(x);
        normaverse    = @(x)        invert(normalize(x));
        norverse      = @(x)        normaverse(x);
        binormalize   = @(x, y)     binarize(normalize(x),y);
        binor         = @(x, y)     binormalize(x,y);
        binormaverse  = @(x, y)     binarize(normaverse(x),y);
        binorverse    = @(x, y)     binormaverse(x,y);
        
        disk          = @(x, y)     imfilter(x,fspecial('disk',y),'replicate');
        gaussian      = @(x, y)     imfilter(x,fspecial('gaussian',round(y*3/2), y/2),'replicate');
        
        HR            = 10;
        retina        = @(x)        FX.Retina(x, evalin('caller', 'HumanFactor')); %@(x)        gaussian(x, evalin('caller', 'HumanFactor'));
        
        retinaFFT     = @(x)        fFFT(retina(x));
        
        crossRFFT     = @(x, y)     mul(retinaFFT(x), retinaFFT(y));
        dotRFFT       = @(x, y)     retinaFFT(x).*retinaFFT(y);
        
        store         = @(n,v)      obj.store(n,v); %eval('assignin(''''caller'''', n, v), v'); %evalin('caller', 'obj.Variables.(' n ') = v;');
        
        level         = @(varargin) imadjust(varargin{:});
        levelFFT      = @(varargin) obj.levelFFT(varargin{:});
        
        iFFTL         = @(varargin) level(inverseFFT(varargin{:}));
        
        logabs        = @(x)        log(abs(x));
        
        processData   = output.ProcessData;
        
        err = [];
        coreVars = [];
        expression = [];
        
        try
          expression  = findField(params, 'expression');
          expression  = strrep(expression, '/', ',');
          expression  = strrep(expression, ':', '=');
        end
        
        
        s = processData.getDataStruct();
        
        structVars(s);
        clear s;
        
        image = output.Image;        
        
        structVars(params);        
        
        coreVars = who;
                
        structVars(output.Variables);
        
        hiContrast    = ReferenceImage.copy;
        hiContrast.Image = imadjust(hiContrast.Image);
        
        patchImage    = PatchImage.Image;
        patchFFT      = PatchImage.FFTData;
        idealImage    = ReferenceImage.Image;
        idealFFT      = ReferenceImage.FFTData;
        contrastImage = hiContrast.Image;
        contrastFFT   = hiContrast.FFTData;
        idealFFT      = ReferenceImage.FFTData;
        screenImage   = HalftoneImage.Image;
        screenFFT     = HalftoneImage.FFTData;
        
        PIMG          = patchImage;
        PFFT          = patchFFT;
        CIMG          = idealImage;
        CFFT          = idealFFT;
        HIMG          = contrastImage;
        HFFT          = contrastFFT;        
        SIMG          = screenImage;
        SFFT          = screenFFT;
        MFFT          = ones(size(SFFT)).*0.5;
        
        %coreVars = who;
        
        RES           = Patch.Resolution;
        CON           = Patch.Contrast;
        RTV           = Patch.Mean;      
        
        SR            = ScanFactor;
        HR            = HumanFactor;
        
        PS            = ceil(Patch.Size*(SR/25.4));
        
        FQ            = RES / (SR/25.4) * PS * 2;
        
        %FQ            = Patch.Resolution / (ScanFactor/25.4) * ceil(Patch.Size*(ScanFactor/25.4)) * 2;
        
        output.Fundamental  = unique([FQ output.Fundamental]);
                        
        try
          eval(expression); %image = eval(expression);
        catch err
          try
            eval(expression);
          end
        end
        
        if exist('I', 'var')
          image = I;
          clear I;
          
          if ~isreal(image)
            
            cpixel = round(size(image)./2);

            cmod = @(x) sqrt((real(x)^2) + (imag(x)^2));

            cval = image(cpixel(1),cpixel(2));
            mod1 = abs(cval);
            mod2 = cmod(cval);

            %disp(['Modulus ' num2str(mod2) ' (' num2str(mod1) ')']);
            % disp(['Modulus ' num2str(cmod(image(cpixel(1),cpixel(2)))) ]);

            [isum fsum rat flt istd fimg] = Grasppe.Kit.ConRes.BandIntensityValue(image, size(image,1), FQ, 1); %, filters{m}, sums{m});

            %disp(['Fundamental ' num2str(FR) ' px ~ ' num2str(rat)]);
            
            if ~exist('Modulus','var') || ~isnumeric(Modulus)
              Modulus = [];
            end
            
            if ~exist('Intensity','var') || ~isnumeric(Intensity)
              Intensity = [];
            end
            
            if ~exist('Labels','var') || ~iscellstr(Labels)
              Labels = {};
            end 
            
            if ~exist('Tally','var') || ~iscell(Tally)
              Tally = {'ID', 'TV', 'CV', 'RV', 'FQ', 'F-Sum', 'F-Area', 'F-Modulus', 'C-Modulus', 'Function'};
            else
              %disp(Tally);
            end 
            
            Modulus(end+1) = mod1;
            Intensity(end+1) = rat;
            Labels{end+1} = Expression;
            
            Tally(size(Tally,1)+1, :) = {ID, RTV, CON, RES, FQ, isum, fsum, rat, mod1, Expression};
            
            %output.Variables.Tally = Tally;
            
            % mat2clip(Tally);
            
%             tallyString = '';
%             
%             for m = 1:size(Tally,1)
%               %tallyString = strcat(tallyString, sprintf('%s\n',sprintf('%s\t', Tally{m,:})));
%               tallyString = strvcat(tallyString, sprintf('%s\t', Tally{m,:}));
%             end
%             
%             clipboard('copy', tallyString(:));
            
            clear cpixel cmod cval flt fimg mod1 mod2 isum fsum rat;
            
          end
          
        end
        
        allVars = who;
        newVars = {};
        
        variables = output.Variables;
        
        for m = 1:numel(allVars)
          try
            n = allVars{m};
            v = eval(n);
            c = any(strcmpi(coreVars, n));
            if c==0
              %disp([n ' = ' toString(v) ' (' class(v) ')']);
              variables.(n) = v;
              newVars{end+1} = n;
            end
          catch err
            debugStamp(err,1);
          end
        end
        
        output.Image = image;
        
        output.Variables = variables;
        
        if ~isreal(image)
          output.Domain = 'frequency';
%           cpixel = round(size(image)./2);
%           
%           cmod = @(x) sqrt((real(x)^2) + (imag(x)^2));
%           
%           cval = image(cpixel(1),cpixel(2));
%           mod1 = abs(cval);
%           mod2 = cmod(cval);
%           
%           disp(['Modulus ' num2str(mod2) ' (' num2str(mod1) ')']);
%           % disp(['Modulus ' num2str(cmod(image(cpixel(1),cpixel(2)))) ]);
%           
%           [isum fsum rat flt istd fimg] = Grasppe.Kit.ConRes.BandIntensityValue(image, size(image,1), FR, 1); %, filters{m}, sums{m});
%           
%           disp(['Fundamental ' num2str(FR) ' px ~ ' num2str(rat)]);
        end
        
        return;
      catch err
        debugStamp(err,1);
      end
    end
    
    function image = store(obj, varargin)
      output  = evalin('caller', 'output');
      image   = evalin('caller', 'image');
      
      output.Variables.(varargin{1}) = varargin{2};
    end
    
    function image = algebra(obj, operation, varargin)
      output  = evalin('caller', 'output');
      image   = evalin('caller', 'image');
      
      %debugging = true;
      
      v = varargin;
      
      try
        switch lower(operation)
          case 'add'
            DBG.dispdbg('Adding...');
            image = v{1}+v{2};
          case 'subtract'
            DBG.dispdbg('Subtracting...');
            image = v{1}-v{2};
          case 'multiply'
            DBG.dispdbg('Multiplying...');
            image = v{1}.*v{2};
          case 'divide'
            DBG.dispdbg('Dividing...');
            image = v{1}./v{2};
          otherwise
            warning('Cannot perform %s operation because it is not support.', toString(operation));
        end
      catch err
        debugStamp(err,1);
      end
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
    
    function image = levelFFT(obj, varargin)
      image     = varargin{1};
      image     = real(log(1+abs(image)));
      imageMin  = min(image(:)); imageMax = max(image(:));
      image     = (image-imageMin) / (imageMax-imageMin);
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
      
      %debugging = true;
      
      method  = 1;
      
      
      switch (output.Domain)
        case 'frequency'
          image = obj.inverseFFT(image);
          output.Domain = 'spatial';
          
        otherwise % case 'spatial'
          
          image = obj.forwardFFT(image);
          output.Domain = 'frequency';
      end
      
      DBG.dispdbg([method varargin{1}]);
    end
  end
end
