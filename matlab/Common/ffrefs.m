function [ output_args ] = ffrefs( input_args )
  %FFREFS Summary of this function goes here
  %   Detailed explanation goes here
  
  refs = {
    'Image Processing - Fourier Transform', 'http://www.mathworks.com/help/toolbox/images/f21-17064.html'
    % '',''
    };
  
  for r = 1:size(refs,1)
    
    disp(sprintf('%s: <a href="matlab: web(''%s'',''-browser'')">%s</a>', refs{r,1}, refs{r,2}, refs{r,2}));
  end
end

