fxProcessor   = Grasppe.ConRes.PatchGenerator.Processors.UserFunction;

fourier       = @(varargin) fxProcessor.fourier(varargin{:});
forwardFFT    = @(varargin) fxProcessor.forwardFFT(varargin{:});
inverseFFT    = @(varargin) fxProcessor.inverseFFT(varargin{:});

fFFT          = @(varargin) forwardFFT(varargin{:});
iFFT          = @(varargin) inverseFFT(varargin{:});

display       = @(varargin) fxProcessor.display(varargin{:});

add           = @(varargin) fxProcessor.algebra('add', varargin{:});
subtract      = @(varargin) fxProcessor.algebra('subtract', varargin{:});
multiply      = @(varargin) fxProcessor.algebra('multiply', varargin{:});
divide        = @(varargin) fxProcessor.algebra('divide', varargin{:});

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

HR            = 10;
retina        = @(x)        disk(x, HR);

retinaFFT     = @(x)        fFFT(retina(x));

crossRFFT     = @(x, y)     mul(retinaFFT(x), retinaFFT(y));

level         = @(varargin) imadjust(varargin{:});
levelFFT      = @(varargin) fxProcessor.levelFFT(varargin{:});

iFFTL         = @(varargin) level(inverseFFT(varargin{:}));

logabs        = @(x)        log(abs(x));
