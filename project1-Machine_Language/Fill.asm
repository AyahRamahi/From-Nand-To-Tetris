// This file is part of www.nand2tetris.org
// and the book "The Elements of Computing Systems"
// by Nisan and Schocken, MIT Press.
// File name: projects/04/Fill.asm

// Runs an infinite loop that listens to the keyboard input.
// When a key is pressed (any key), the program blackens the screen,
// i.e. writes "black" in every pixel;
// the screen should remain fully black as long as the key is pressed. 
// When no key is pressed, the program clears the screen, i.e. writes
// "white" in every pixel;
// the screen should remain fully clear as long as no key is pressed.

// Put your code here.

// MAKE SCREEN WHITE , INITIALIZE VARIABLE TO COLOR WHITE (0)
	@status
	M=0
	@COLOR
	0;JMP
// CHECK STATUS OF KBD AND PUT 0 OR -1 IN STATUS
(LOOP)
	@KBD
	D=M
// KBD==0 GO TO WHITE ELSE GO TO BLACK
	@WHITE
	D;JEQ
(BLACK)
// IF STATUS != 0 LOOP
	@status
	D=M
	@LOOP
	0;JNE
// ELSE
	@status
	M=-1
	@COLOR
	0;JMP
(WHITE)
// IF STATUS == 0 LOOP
	@status
	D=M
	@LOOP
	D;JEQ
// ELSE
	@0
	D=A
	@status
	M=D
///////////////////////////////////
(COLOR)
// PUT 8192 IN I
	@8192
	D=A
	@i
	M=D
// PUT SCREEN IN ADD
	@SCREEN
	D=A
	@add
	M=D
(COLORLOOP)
// PUT IN THE PIXEL STATUS
	@status
	D=M
	@add
	A=M
	M=D
// ADD 1 TO ADD
	@add
	M=M+1
// SUB FROM 1 FROM I
	@i
	M=M-1
	D=M
// IF I == 0 EXIT COLORING
	@EXIT
	D;JEQ
	@COLORLOOP
	0;JMP
////////////////////////////////////////////////
(EXIT)
// IF FINISHED COLORING GO CHECK AGAIN
	@LOOP
	0;JMP
