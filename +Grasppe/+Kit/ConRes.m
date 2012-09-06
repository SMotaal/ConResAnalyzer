classdef ConRes
  %DEFAULTS Summary of this class goes here
  %   Detailed explanation goes here
  
  properties
    IMPORTS = {'Grasppe.Imaging.Patterns.*' , 'Grasppe.Kit.*'};
    
    TONE_RANGE        = 5:5:95;
    CONTRAST_RANGE    = [100 70.17 49.239 34.551 24.245 17.013 11.938 8.377 5.878 4.125 2.894 2.031 1.425 1.0];
    % Grasppe.Kit.LogSeries(1, 100, 14);
    % [100 68.1 46.4 31.6 21.5 14.7 10.0 6.8 4.6 3.2 2.2 1.5 1.0];
    RESOLUTION_RANGE  = [0.625	0.746	0.891	1.064	1.269	1.515	1.812	2.155	2.577	3.086	3.676	4.386	5.208	6.25];
    % Grasppe.Kit.LogSeries(a, b, c);     % [0.63	0.75	0.89	1.06	1.27	1.52	1.81	2.16	2.58	3.09	3.68	4.39	5.21	6.25];
    
    DEFAULT_PATCH_WIDTH     = 5.3;  % mm
    DEFAULT_ADDRESSIBILITY  = 2450; % spi
    DEFAULT_RESOLUTION      = 0.63; % lppmm
  end
  
  methods (Access=private)
    
    function obj = ConRes()
    end
    
  end
  
  methods (Static)
    
    function [img spec] = GeneratePatchImage(reference, contrast, resolution, width, addressibility)
      
      Grasppe.Kit.ConRes.GetImports;...
        import(Imports{:}); ...
        ConRes.GetInstance;
      
      if ~exist('reference', 'var') || isempty(reference)
        reference = 50;
      end
      
      if ~exist('contrast', 'var') || isempty(contrast)
        contrast = 100;
      end
      if ~exist('width', 'var') || isempty(width)
        width = Instance.DEFAULT_PATCH_WIDTH; % mm
      end
      
      if ~exist('addressibility', 'var') || isempty(width)
        addressibility = Instance.DEFAULT_ADDRESSIBILITY;
      end
      
      if ~exist('resolution') || isempty(width)
        resolution = Instance.DEFAULT_RESOLUTION; % lppmm
      end
      
      cpmm    = resolution;
      cpin    = cpmm * 25.4;
      cppx    = cpin / addressibility;
      
      cycles  = width * cpmm;
      pixels  = cycles / cppx;
      
      
      [img rg ct rtv]   = ConcentricCircles(cppx*100, reference, contrast, ceil(pixels));
      
      spec = [100*(1-rtv) 100*ct resolution];
      
      %image = []; %Grasppe.Imaging.Patterns.ConcentricCircles(cycles, );
    end
    
  end
  
  
  methods (Static)  % Getters
    function value = ContrastRange()
      Grasppe.Kit.ConRes.GetInstance;
      value     = Instance.CONTRAST_RANGE;
    end
    
    function value = ResolutionRange()
      Grasppe.Kit.ConRes.GetInstance;
      value     = Instance.RESOLUTION_RANGE;
    end
    
    function value = ToneRange()
      Grasppe.Kit.ConRes.GetInstance;
      value     = Instance.TONE_RANGE;
    end
    
    function rexp = LogSeries(a, b, c)
      
      rexp = Grasppe.Kit.LogSeries(a, b, c);
      %r = b/a; rlog = log(r)/(c-1); r2 = [0:c-1] * rlog; rexp = exp(r2);
    end
    
    function [instance class] = GetInstance()
      persistent Instance; ...
        Class = eval(NS.CLASS);
      
      Instance = eval(Grasppe.Kit.GetInstance); ...
        instance = Instance; ...
        class    = Class;
      
      if nargout == 0
        assignin('caller', 'Instance', Instance);
      end
    end
    
    function [imports] = GetImports
      eval([eval(NS.CLASS) '.GetInstance;']);
      
      imports = Instance.IMPORTS;
      
      assignin('caller', 'Imports', imports);
      
      % for m = 1:numel(imports)
      %   evalin('caller', ['import(''' imports{m} ''');']);
      % end
      % evalin('caller', 'import(''Imports{:}'');');
    end
    
%     function [fQ currentData] = CalculateBandIntensity(fImg)
%       
% %       persistent bandFilters bandSize bandSums;
%             
%       s = warning('off', 'all');
%       
%       try
%         M = size(fImg,1);
%         N = size(fImg,2);
%         nBands = floor(min(size(fImg))/7);
%         fQ = zeros(1, nBands);
%         
%         
%         currentData = zeros(nBands, 5);
%         
%         aImg = abs(fImg);
%         
%         parfor m = 1:nBands %min(nBands, 70)
%             %[isum fsum rat flt istd fimg] = Grasppe.Kit.ConRes.BandIntensityValue(fImg, fH, m, 3); %, filters{m}, sums{m});
%             
%             [flt  fsum] = Grasppe.Kit.ConRes.GaussianBandfilter(m, 3, M, N);
%             
%             img         = aImg.*flt;
%             isum        = sum(img(:));
%             istd        = std(img(flt~=0));
%             rat         = isum / fsum;
%             
%             fQ(m)   = rat;
%             
%             currentData(m,:) = [m isum fsum rat istd];
%         end
%                 
%       catch err
%         debugStamp(err, 1); %disp(err);
%       end
%       
%       warning(s);
%     end
    
    function [fQ currentData] = CalculateBandIntensity(fImg, nBands, W)
      
      persistent Filters Sums;
            
      s = warning('off', 'all');
      
      try
        M = size(fImg,1);
        N = size(fImg,2);
        if ~exist('nBands', 'var') || ~isscalar(nBands) || ~isnumeric(nBands) || nBands>min(M, N)/2
          nBands = floor(min(size(fImg))/7);
        else
          nBands = floor(nBands);
          debugStamp('Using custom nBands!', 5);
        end
        
        if ~exist('W', 'var') || ~isscalar(W) || ~isnumeric(W) || W<0 || W*10>min(M, N)/2 %min(M, N)/2
          W = 3;
        end
        
        fQ = zeros(1, nBands);
        
        
        currentData = zeros(nBands, 6);
        
        aImg    = fImg;
        aImg    = abs(aImg);
        %if ~isreal(aImg), aImg = abs(aImg); end
        
        BL      = 0;
        filters = Filters;
        sums    = Sums;
        
        if isempty(filters)
          filters = cell(1, nBands);
          sums    = cell(1, nBands);
        end
        
        for B = 1:nBands         

            W               = 3;
            Q               = 2*(B+W+2);
            
            F               = [];
            S               = [];            

            try
              if B>BL
                F           = filters{B};
                S           = sums{B};
              end
            end
            %end
            
            if isempty(F) %|| force
              F             = fftshift(bandfilter('gaussian', 'pass', Q, Q, B, W));
              if B>BL
                filters{B}  = F; %{W,B}  = F;
              end
            end
            
            if ~isscalar(S) %|| force
              S             = sum(F(:));
              if B>BL
                sums{B}     = S;
              end
            end
            
            ry            = [1:Q] + ceil((M-Q)/2);
            rx            = [1:Q] + ceil((N-Q)/2);
            
            f = F~=0;
            img         = aImg;
            img         = img(ry, rx);
            img         = img(f).*F(f);
            
            isum        = sum(img);
            
            nimg       	= S;%numel(img);
            imean       = isum/nimg;
            %n           = S-1;
            % if n > 0 % avoid divide-by-zero
            %     xbar = sum(x, dim) ./ n;
            %     x = bsxfun(@minus, x, xbar);
            % end
            % y = sum(abs(x).^2, dim) ./ denom; % abs guarantees a real result
            
            istd        = sqrt(sum((img-(isum/nimg)).^2)./nimg); %sqrt(sum((img-(isum/nimg)).^2)./nimg); %sum(img.^2)./S; %std(img,1)%
            mstd        = istd; %std(img,1);
            
            %istd        = 1/(S-1)*sqrt(sum(img.^2)-S*imean^2);
            
            currentData(B,:) = [B isum S imean istd mstd];
        end
        
        fQ = currentData(:,5);
        
        Filters = filters;
        Sums    = sums;
        
        if nargout==1, fQ = currentData; end
                
      catch err
        debugStamp(err, 1);
      end
      
      warning(s);
    end
    
    
    
    function [F S] = GaussianBandfilter(B, W, M, N, force)
      
      persistent indices filters sums;
      
      try

      I         = [];
      F         = [];
      S         = [];
      
      force     = exist('force', 'var') && isequal(force, true);
      Q         = 2*(B+W+2);
      
      w = ['W' int2str(W*100)];
      b = ['B' int2str(B*100)];
      
      t = [w b];

      %% Look for the filter
      try
        %[c I]   = intersect(indices, [B W], 'rows');
        %I       = I(1);
        %F       = filters{I};
        %S       = sums(I);
        F        = filters.(t);%filters{W,B};
        S        = sums.(t); %{W,B};
      end
      
      %% Generate the filter
      if isempty(F) || force
        
        F     = fftshift(bandfilter('gaussian', 'pass', Q, Q, B, W));
        
%         if ~isscalar(I) || ~isnumeric(I)
%           I 	= size(indices, 1)+1;
%         end
        if Q>15
          filters.(t) = F; %{W,B}  = F;
        end
        %filters{I}    = F;
        %indices(I, :) = [B W];
        
%         nFilters        = indices;
%         if size(indices,1) > 1000
%           indices       = indices(2:nFilters, :);
%           filters       = filters(2:nFilters);
%           sums          = sums(2:nFilters);
%         end
      end
      
      if ~isscalar(S) || force
        S             = sum(F(:));
        if Q>15
          sums.(t) = S; %filters.(t) = F; %{W,B}  = F;
        end        
        %{W,B}     = S;
%         sums(I)       = S;
      end
      
      %% Pad the filter
      G             = zeros(M, N);
      ry            = [1:Q] + ceil((M-Q)/2);
      rx            = [1:Q] + ceil((N-Q)/2);
      G(rx, ry)     = F;
            
      if nargout>0, F = G; end
      %if nargout>1, S = S; end
      
    
      catch err
        debugStamp(err,1);
        rethrow(err);
      end
    end
    
    function [isum fsum rat flt istd fimg] = BandIntensityValue(img, sz, bnd, wd, flt, fsum)
      
      if nargin<4 || ~isscalar(wd) || ~isnumeric(wd)
        wd    = 1;
      end
      
%       if nargin<5 || isempty(flt) || ~isnumeric(flt);
%         flt   = fftshift(bandfilter('gaussian', 'pass', sz, sz, bnd, wd));
%         fsum  = [];
%       end
%       
%       if nargin<6 || isempty(fsum) || ~isscalar(fsum) || ~isnumeric(fsum);
%         fsum  = sum(flt(:));
%       end

      [flt, fsum] = Grasppe.Kit.ConRes.GaussianBandfilter(bnd, wd, sz, sz);
      
      fimg    = img.*flt;
      isum    = sum(fimg(:));
      istd    = std(fimg(flt~=0));
      rat     = isum / fsum;
      
      %disp([bnd isum fsum rat]);
      
    end
    
          
  end
  
  
end

