classdef ProcessImage < matlab.mixin.Copyable
  %IMAGEPARAMETERS Summary of this class goes here
  %   Detailed explanation goes here
  
  properties (Dependent)
    Width
    Height
    Depth
    Resolution
    Unit
    Mode
    Process
    ProcessData
    Image
    Fourier
    FourierImage
    Domain
  end
  
  properties
    Variables = struct;
    Snapshots = {};
    PlotFFT = true;
    fftimage;
    ffttimer;
  end
  
  properties (Access=private)
    width
    height
    depth
    resolution  = 2400;
    unit        = 'Inch';
    mode
    process     = {};
    processData = Grasppe.Occam.ProcessData.empty;
    image
    domain      = 'spatial';
    % variables   = struct;
  end
  
  methods
    
    function obj = ProcessImage()
      obj.ffttimer = timer('Tag','ImageProcessor', 'StartDelay', 1, ...
        'TimerFcn', @(x,y) obj.generatePlotFFT);
    end
    
    function delete(obj)
      try delete(obj.ffttimer); end
    end
    
    function width = get.Width(obj)
      width = obj.width;
    end
    
    function set.Width(obj, width)
      if ~isequal(obj.width, width)
        obj.width = width;
      end
    end
    
    function height = get.Height(obj)
      height = obj.height;
    end
    
    function set.Height(obj, height)
      if ~isequal(obj.height, height)
        obj.height = height;
      end
    end
    
    function depth = get.Depth(obj)
      depth = obj.depth;
    end
    
    function set.Depth(obj, depth)
      if ~isequal(obj.depth, depth)
        obj.depth = depth;
      end
    end
    
    function resolution = get.Resolution(obj)
      resolution = obj.resolution;
    end
    
    function set.Resolution(obj, resolution)
      if ~isequal(obj.resolution, resolution)
        obj.resolution = resolution;
      end
    end
    
    function unit = get.Unit(obj)
      unit = obj.unit;
    end
    
    function set.Unit(obj, unit)
      if ~isequal(obj.unit, unit)
        obj.unit = unit;
      end
    end
    
    function mode = get.Mode(obj)
      mode = obj.mode;
    end
    
    function set.Mode(obj, mode)
      if ~isequal(obj.mode, mode)
        obj.mode = mode;
      end
    end
    
    function process = get.Process(obj)
      process = obj.process;
    end
    
    %     function set.Process(obj, process)
    %       if ~isequal(obj.process, process)
    %         obj.process = process;
    %       end
    %     end
    
    function setImage(obj, image, resolution)
      obj.Image       = image;
      obj.Resolution  = resolution;
      
    end
    
    function processData = get.ProcessData(obj)
      processData = obj.processData;
    end
    
    function addProcess(obj, process)
      try
        processData = process.getProcessData;
        obj.process{end+1}      = process;
        obj.processData(end+1)  = processData;
      end
    end
    
    function image = get.Image(obj)
      image = obj.image;
    end
    
    function image = get.Fourier(obj)
      image = obj.image;
      if isreal(image)
        image = obj.forwardFFT(image);
      end
%       switch (obj.Domain)
%         case 'frequency'
%         otherwise
%           image = obj.forwardFFT(image);
%       end
    end
    
    function image = get.FourierImage(obj)
      obj.generatePlotFFT();
      image = obj.fftimage;
      %if isempty(image)
      %obj.generatePlotFFT;
      %         image = obj.Image;
      %         switch (obj.Domain)
      %           case 'frequency'
      %           otherwise
      %             image = obj.forwardFFT(image);
      %         end
      %obj.fftimage = image;
      
      %image = obj.bandPlotFFT(image);
      %end
      
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
    
    function generatePlotFFT(obj)
      stop(obj.ffttimer);
      fftdata = obj.Fourier;
      
      if isreal(fftdata)
        fftdata = obj.forwardFFT(fftdata);
      end
      
      if isempty(obj.fftimage)
        obj.bandPlotFFT([]);
        fftimage = [];
        while isempty(fftimage)
          fftimage = obj.bandPlotFFT(fftdata);
          pause(0.5);
        end
        obj.fftimage = fftimage;
      end
    end
    
    function image = bandPlotFFT(obj, image)
      
      persistent  fxBusy; %hFig hAxis
      
      if isempty(image)
        fxBusy = false;
        %try delete(hAxis);  end
        %try delete(hFig);   end
        %return;
      end
      
      if isequal(fxBusy, true)
        image = [];
        return;
      end
      
      try
        
        fxBusy=true;
        disp('Generating Image...');
        R=tic;
        try
          
          image1 = image;
          
          if size(image1,3) > 1
            image1 = image1(:,:,1);
            disp('Flattening Image...');
          end
          
          
          image1     = real(log(1+abs(image1)));
          imageMin  = min(image1(:)); imageMax = max(image1(:));
          image1     = (image1-imageMin) / (imageMax-imageMin);
          
          if isequal(obj.PlotFFT, true)
            
            %if ~isscalar(hFig) || ~ishandle(hFig)
              hFig  = figure('Visible', 'off', 'Position',[-1000 -1000 300 300]);
            %  hAxis = [];
            %end
            
            %if ~isscalar(hAxis) || ~ishandle(hAxis)
              hAxis = axes('Parent', hFig); % 'Visible', 'off'
            %end
            
            disp('Generating Plot...');
            
            bFq = Grasppe.Kit.ConRes.CalculateBandIntensity(image1); %hold on; plot(bFq);
            
            yR  = bFq/max(bFq(:));
            xR  = 1:numel(bFq);
            zR  = ones(size(xR));
            
            xF  = 0.75;
            xD  = (size(image1,2)/4*1.25);            
            yD  = size(image1,1)/2;
            
            %hold on;
            
            cla(hAxis);
            
            image2 = repmat(image1, [1, 1, 3]);
            
            imshow(image2, 'Parent', hAxis);
            truesize(hFig);
            
            hold on;
            
            yR = (yR*100)-(max(yR*100)/2);
                       
            yN = 1;
            for m = 1:20:numel(xR) %min(xR):5:max(xR)
              x = [ 0 0]  + (xD+xR(m)*xF);
              if yN==1
                y = [-25 25]  + yD + nanmean(yR); %+yR(m);
                yN = 0;
              else
                y = [-10 10]  + yD + nanmean(yR); % +yR(m);
                yN = 1;
              end
              line(x+0.5, y, [0 0], 'Parent', hAxis, 'Color', 'w', 'Linewidth', 0.25, 'linesmoothing','on');
            end
            
            plot(hAxis, xD+xR*xF,yD+yR, 'g', 'Linewidth', 2,'linesmoothing','on');
            
            disp('Exporting Image...');
            
            image = export_fig(hFig); %, [1 1 size(image2,2) size(image2,1)]);
            
            delete(hAxis);
            delete(hFig);
          else
            image = image1;
          end
          
        catch err
          disp err
        end
        toc(R);
      end
      
      fxBusy=false;
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
    
    
    
    function set.Image(obj, image)
      if ~isequal(obj.image, image)
        obj.image = image;
        obj.updateMetadata();
      end
    end
    
    function Snap(obj, id)
      newObj = copy(obj);
      
      obj.Snapshots{end+1,1} = newObj; %.Image;
      
      obj.Snapshots{end,2}   = id;
      
      width   = obj.Width;
      height  = obj.Height;
      
      if (width*height < 600*600)
        start(newObj.ffttimer);
      else
        dispf('Not generating %d x %d', width, height);
      end
    end
    
    function updateMetadata(obj)
      image   = obj.image;
      
      width   = size(image, 2);
      height  = size(image, 2);
      depth   = ByteSize(image(1,1,1));
      
      mode    = obj.mode;
      
      switch size(image,3)
        case 1
          if depth < 1
            mode  = 'grayscale';
          else
            mode  = 'bitmap';
          end
        case 2
          mode = 'duotone';
        case 3
          if isempty(mode) || isequal(mode, 'rgb')
            mode = 'rgb';
          end
        case 4
          mode = 'cmyk';
        otherwise
          mode = 'multichannel';
      end
      
      obj.width   = width;
      obj.height  = height;
      obj.depth   = depth;
      obj.mode    = mode;
      
      
      obj.fftimage = [];
      
      %       obj.ffttimer = timer('Tag','ImageProcessor', 'StartDelay', 0.5, ...
      %         'TimerFcn', @(x,y) obj.generatePlotFFT);
      %      if (width*height < 600*600)
      %        start(obj.ffttimer);
      %      else
      %        dispf('Not generating %d x %d', width, height);
      %      end
      %obj.fftimage = obj.bandPlotFFT(image);
      
    end
    
    
    function domain = get.Domain(obj)
      domain = obj.domain;
    end
    
    function set.Domain(obj, domain)
      if ~isequal(obj.domain, domain)
        obj.domain = domain;
      end
    end
    
    % function variables = get.Variables(obj)
    %   variables = obj.variables;
    % end
    %
    % function set.Variables(obj, variables)
    %   if ~isequal(obj.variables, variables)
    %     obj.variables = variables;
    %   end
    % end
    
  end
  
  methods(Access = protected)
    % Override copyElement method:
    function cpObj = copyElement(obj)
      % Make a shallow copy of all four properties
      cpObj = copyElement@matlab.mixin.Copyable(obj);
      cpObj.fftimage = [];
      cpObj.bandPlotFFT([]);
      cpObj.ffttimer = timer('Tag','ImageProcessor', 'StartDelay', 1, ...
        'TimerFcn', @(x,y) obj.generatePlotFFT); %copy(obj.ffttimer);
      % Make a deep copy of the DeepCp object
      %cpObj.DeepObj = copy(obj.DeepObj);
    end
  end
  
end

