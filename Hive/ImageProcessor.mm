//
//  ImageProcessor.m
//  Hive
//
//  Created by Denis Kubasov on 14/06/15.
//  Copyright (c) 2015 Denis Kubasov. All rights reserved.
//

#import <Foundation/Foundation.h>

#import <AppKit/NSImage.h>

#include "ImageProcessor.h"

#include "Harris.h"

@implementation ImageProcessor

struct vuy {
   union {
   unsigned char v;
   unsigned char u;
   };
   unsigned char y;
} __attribute__((packed));

struct HarrisRef {
   features::HarrisDetector *harris;
};

- (id) init {
   self = [super init];
   if (self!=nil) {
      _drawLayer = [CALayer layer];
      _drawLayer.contentsGravity = kCAGravityResizeAspect;
      
      _detector.harris = new features::HarrisDetector;
   }
   return self;
}

- (void) mirroringChanged: (bool) mirrored {
   _mirrored = mirrored;
}

- (void) inputChanged: (AVCaptureDeviceInput*) input {
   
   delete[] _buffer;
   CMFormatDescriptionRef desc = input.device.activeFormat.formatDescription;
   CMVideoDimensions dim = CMVideoFormatDescriptionGetDimensions(desc);
   
   _width = dim.width / 2;
   _height = dim.height / 2;
   size_t bufferLength = _width * _height;
   _buffer = new unsigned char[bufferLength];
   
   unsigned char* dst = _buffer;
   for(int i = 0; i < _height; ++i) {
      for(int j = 0; j < _width; ++j) {
         if(i == 100)
            dst[j] = 255;
         else
            dst[j] = 0;
      }
      dst += _width;
   }
   
   _provider = CGDataProviderCreateWithData(NULL, _buffer, bufferLength, NULL);

   printf("Created image of %dx%d\n", dim.width, dim.height);
}

- (void)processFrame:(CVPixelBufferRef)pixelBuffer {

   size_t w = CVPixelBufferGetWidth(pixelBuffer);
   size_t h = CVPixelBufferGetHeight(pixelBuffer);
   //printf("it's a pixel data! [%zu,%zu]\n", w, h);
   
   NSTimeInterval timeInMiliseconds = [[NSDate date] timeIntervalSince1970];
   
   CVPixelBufferLockBaseAddress(pixelBuffer, 0);
   size_t rowbytes = CVPixelBufferGetBytesPerRow(pixelBuffer);
   
   struct vuy *ptr = (struct vuy *) CVPixelBufferGetBaseAddress(pixelBuffer);
   unsigned char* dst = _buffer;
   
   long scale_x = w / _width;
   
   for(int i = 0; i < _height; ++i) {
      struct vuy *p = ptr;
      if(!_mirrored) {
         for(int j = 0; j < _width; ++j, p += scale_x) {
            dst[j] = p->y;
         }
      } else {
         for(int j = 0; j < _width; ++j, p += scale_x) {
            dst[w - j - 1] = p->y;
         }
      }
      ptr = (struct vuy *)(((char *) ptr) + rowbytes * h / _height);
      dst += _width;
   }
   
   features::Image image{_width, _height, _width, _buffer};
   int count = _detector.harris->findFeatures(image);
   
   size_t bitsPerComponent = 8;
   size_t bitsPerPixel = 8;
   size_t bytesPerRow = _width;

   CGImageRelease(_imageRef);
   _imageRef = CGImageCreate(_width,
                             _height,
                             bitsPerComponent,
                             bitsPerPixel,
                             bytesPerRow,
                             CGColorSpaceCreateDeviceGray(),
                             kCGBitmapByteOrderDefault,
                             _provider,   // data provider
                             NULL,       // decode
                             YES,        // should interpolate
                             kCGRenderingIntentDefault);

   _drawLayer.contents = (__bridge id)(_imageRef);
   [_drawLayer.superlayer setNeedsDisplay];
   [CATransaction flush];

   printf("%f: Got new frame %zu x %zu with base adress %p bytes per row %zu\n", timeInMiliseconds, w, h, CVPixelBufferGetBaseAddress(pixelBuffer), rowbytes);

   CVPixelBufferUnlockBaseAddress(pixelBuffer, 0);

}

@end
