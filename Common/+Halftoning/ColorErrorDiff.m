function ret = ColorErrorDiff(FS, imR, imG, imB)

% RET = ColorErrorDiff(FS, IMR, IMG, IMB)
% ColorErrorDiff performs a color error diffusion algorithm
% which takes the color subregion into account when
% determining potential output colors, limiting the variation
% in output for a continuos tone.  Basic error diffusion technique
% is based on Floyd Steinberg method, but adapted to run with RGB
% values of 0-1, not just grayscale. IMR, IMG and IMB are the component
% Red, Green and Blue images respectively
% Returns a N x M x 3 variable
% FS is the Error Filter for Error Diffusion
%
% SEE ALSO GENSCREEN, COLORDITHER, runVecFiltOpt, VECDIFF
%
% Ref: "Color Diffusion: Error-Diffusion for Color Halftones
% by Shaked, Arad, Fitzhugh, and Sobel -- HP Labs
% Hewlett-Packard Laboratories TR 96-128
% and Electronic Imaging, Proc. SPIE 3648, 1999

% Vishal Monga, July 2002

% Part of Halftoning Toolbox Version 1.2 released July 2005

% Copyright (c) 1999-2005 The University of Texas
% All Rights Reserved.


% determine size of image and Floyd Steinberg parameter
[img_r, img_c] = size(imR);
xFS_r = size(FS, 1);
xFS_c = fix(size(FS, 2)/2);

% create working areas for R, G, and B values ...

imNewR = zeros(img_r+xFS_r, img_c+2*xFS_c);
imNewR(1:img_r, xFS_c +1:xFS_c+img_c) = imR;
imNewB = zeros(img_r+xFS_r, img_c+2*xFS_c);
imNewB(1:img_r, xFS_c +1:xFS_c+img_c) = imB;
imNewG = zeros(img_r+xFS_r, img_c+2*xFS_c);
imNewG(1:img_r, xFS_c +1:xFS_c+img_c) = imG;

% and SEPARATE storage matrices for error, since this method
% uses the original vaues to calculate the color MBVQ
% (Minimum Brightness Variation Quadruple)

imER = zeros(img_r+xFS_r, img_c+2*xFS_c);
imEG = zeros(img_r+xFS_r, img_c+2*xFS_c);
imEB = zeros(img_r+xFS_r, img_c+2*xFS_c);

%disp('Performing color diffusion - patience ...')

for ir = 1 : img_r,     % master error diffusion loop
    for ic = xFS_c+1 : img_c,

        
Ro = imNewR(ir, ic);
Go = imNewG(ir, ic);
Bo = imNewB(ir, ic);

R = Ro + imER(ir,ic);
G = Go + imEG(ir,ic);
B = Bo + imEB(ir,ic);

% debug
%eR = 0.1
%eG = 0.1
%eB = 0.1
%Ro = 1;
%Bo = 1;
%Go = 1;
%R = 1 + eR;
%B = 1 + eR;
%G = 1 + eR;


% define subregion values

% Outer if statements determine 

    if((Ro+Go) > 1)
        if((Go+Bo) > 1)
            if ((Ro+Go+Bo) > 2)
                
                % for CMYW
                % disp('CMYW')
                rc = [1 1 1]; % white unless ...
                if (B < 0.5)
                    if (B <= R)
                        if (B <= G)
                            rc = [1 1 0]; % yellow
                        end
                    end
                end
                if (G < 0.5)
                    if (G <= B)
                        if (G <= R)
                            rc = [1 0 1]; % magenta
                        end
                    end
                end
                if (R < 0.5)
                    if (R <= B)
                        if (R <= G)
                            rc = [0 1 1]; % cyan
                        end
                    end
                end
            else
                % for MYGC
                % disp('MYGC')
                rc = [1 0 1]; % magenta unless ..
                if (G >= B)
                    if (R >= B)
                       if (R >= 0.5)
                           rc = [1 1 0]; % yellow
                       else
                           rc = [0 1 0]; % green
                       end
                    end
                end
                if (G >= R)
                    if (B >= R)
                        if (B >= 0.5)
                            rc = [0 1 1]; % cyan 
                        else
                            rc = [0 1 0]; % green
                        end
                    end
                
                end
            end
        else
            % for RGMY
            % disp('RGMY')
            if (B > 0.5)
                if (R > 0.5)
                    if (B >= G)
                        rc = [1 0 1]; % magenta
                    else
                        rc = [1 1 0]; % yellow
                    end
                else
                    if (G > B + R)
                        rc = [0 1 0]; % green
                    else 
                        rc = [1 0 1]; % magenta
                    end
                end
            else
                if (R >= 0.5)
                    if (G >= 0.5)
                        rc = [1 1 0]; % yellow
                    else
                        rc = [1 0 0]; % red
                    end
                else
                    if (R >= G)
                        rc = [1 0 0];
                    else
                        rc = [0 1 0]; % green
                    end
                end
            end
        end
    else
        if( not((Go+Bo)>1) )
            if(not((Ro+Go+Bo) > 1))
                % disp('KRGB')
                % use KRGB
                rc = [0 0 0]; % black unless ...
                if (B > 0.5)
                    if (B >= R)
                        if (B >= G)
                            rc = [0 0 1]; % blue
                        end
                    end
                end
                if (G > 0.5)
                    if (G >= B)
                         if (G >= R)
                              rc = [0 1 0]; % green
                        end
                    end
                end
                if (R > 0.5)
                    if (R >= B)
                        if (R >= G)
                            rc = [1 0 0]; % red
                        end
                    end
                end
            else
                % use RGBM
                %disp('RGBM')
                rc = [0 1 0]; % green unless ..
                if (R > G)
                    if (R >= B)
                       if (B < 0.5)
                           rc = [1 0 0]; % red
                       else
                           rc = [1 0 1]; % magenta
                       end
                    end
                end
                if (B > G)
                    if (B >= R)
                       if (R < 0.5)
                          rc = [0 0 1]; % blue
                       else
                          rc = [1 0 1]; % magenta
                       end
                   end
                end
            end
        else
            %use CMGB
            % disp('CMGB')
            if (B > 0.5)
                if ( R > 0.5)
                    if (G >= R)
                        rc = [0 1 1]; % cyan
                    else
                        rc = [1 0 1]; % magenta
                    end
                else
                    if (G > 0.5)
                        rc = [0 1 1]; % cyan
                    else
                        rc = [0 0 1]; % blue
                    end
                end
            else
                if ( R > 0.5)
                    if (R - G + B >= 0.5)
                        rc = [1 0 1]; % magenta
                    else
                        rc = [0 1 0]; % green
                    end
                else
                    if (G >= B)
                        rc = [0 1 0]; % green
                else
                        rc = [0 0 1]; % blue
                    end
                end
            end
        end
    end

% debug
% rc

    
% calculate error to pass
imNewR(ir,ic) = rc(1);
imNewG(ir,ic) = rc(2);
imNewB(ir,ic) = rc(3);

error = zeros(1,3);
errorRfs = (R - rc(1)) * FS;
errorGfs = (G - rc(2)) * FS;
errorBfs = (B - rc(3)) * FS;
imER(ir:ir+xFS_r-1, ic-xFS_c:ic+xFS_c) = ...
imER(ir:ir+xFS_r-1, ic-xFS_c:ic+xFS_c)+errorRfs;
imEG(ir:ir+xFS_r-1, ic-xFS_c:ic+xFS_c) = ...
imEG(ir:ir+xFS_r-1, ic-xFS_c:ic+xFS_c)+errorGfs;
imEB(ir:ir+xFS_r-1, ic-xFS_c:ic+xFS_c) = ...
imEB(ir:ir+xFS_r-1, ic-xFS_c:ic+xFS_c)+errorBfs;
end;

imER(ir:ir+xFS_r-1,img_c+1:img_c+xFS_c) = ...
imER(ir:ir+xFS_r-1,img_c+1:img_c+xFS_c)+imER(ir+1:ir+xFS_r, 1:xFS_c);
imEG(ir:ir+xFS_r-1,img_c+1:img_c+xFS_c) = ...
imEG(ir:ir+xFS_r-1,img_c+1:img_c+xFS_c)+imEG(ir+1:ir+xFS_r, 1:xFS_c);
imEB(ir:ir+xFS_r-1,img_c+1:img_c+xFS_c) = ...
imEB(ir:ir+xFS_r-1,img_c+1:img_c+xFS_c)+imEB(ir+1:ir+xFS_r, 1:xFS_c);

for ic = img_c+1 : img_c+xFS_c,
    errorRfs = imER(ir,ic) * FS;
    errorGfs = imEG(ir,ic) * FS;
    errorBfs = imEB(ir,ic) * FS;
    imER(ir:ir+xFS_r-1, ic-xFS_c:ic+xFS_c) = ...
    imER(ir:ir+xFS_r-1, ic-xFS_c:ic+xFS_c)+errorRfs;
    imEG(ir:ir+xFS_r-1, ic-xFS_c:ic+xFS_c) = ...
    imEG(ir:ir+xFS_r-1, ic-xFS_c:ic+xFS_c)+errorGfs;
    imEB(ir:ir+xFS_r-1, ic-xFS_c:ic+xFS_c) = ...
    imEB(ir:ir+xFS_r-1, ic-xFS_c:ic+xFS_c)+errorBfs;
end

imER(ir+1:ir+xFS_r, xFS_c+1:2*xFS_c) = ...
imER(ir+1:ir+xFS_r, xFS_c+1:2*xFS_c) + imER(ir:ir+xFS_r-1,  ...
img_c+xFS_c+1:img_c+2*xFS_c);
imNewR(:, 1:xFS_c) = zeros(img_r+xFS_r, xFS_c);
imNewR(:, img_c+xFS_c+1:img_c+2*xFS_c) = zeros(img_r+xFS_r, xFS_c);

imEG(ir+1:ir+xFS_r, xFS_c+1:2*xFS_c) = ...
imEG(ir+1:ir+xFS_r, xFS_c+1:2*xFS_c) + imEG(ir:ir+xFS_r-1,  ...
img_c+xFS_c+1:img_c+2*xFS_c);
imNewG(:, 1:xFS_c) = zeros(img_r+xFS_r, xFS_c);
imNewG(:, img_c+xFS_c+1:img_c+2*xFS_c) = zeros(img_r+xFS_r, xFS_c);

imEB(ir+1:ir+xFS_r, xFS_c+1:2*xFS_c) = ...
imEB(ir+1:ir+xFS_r, xFS_c+1:2*xFS_c) + imEB(ir:ir+xFS_r-1,  ...
img_c+xFS_c+1:img_c+2*xFS_c);
imNewB(:, 1:xFS_c) = zeros(img_r+xFS_r, xFS_c);
imNewB(:, img_c+xFS_c+1:img_c+2*xFS_c) = zeros(img_r+xFS_r, xFS_c);

end % of master error diffusion loop


%DisplayRGB(imNewR, imNewG, imNewB);

%DisplayCMY(1 - imNewR,1 - imNewG,1 - imNewB);
ret(:,:,1) = imNewR(1:img_r,1:img_c);
ret(:,:,2) = imNewG(1:img_r,1:img_c);
ret(:,:,3) = imNewB(1:img_r,1:img_c);


