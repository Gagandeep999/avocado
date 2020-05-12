% PROGRAM START
           entry
% processing t1 stores 5
           addi R1, R0, 5
           sw lit_t1(R0), R1

% processing t1 stores 5
          addi R1, R0, 6
          sw lit_t2(R0), R1

          lw R2, lit_t1(R0)
          lw R3, lit_t2(R0)
          ceq R4, R2, R3
          sw lit_t3(R0), R4

          lw R1, lit_t3(R0)
          bz R1, x1
          addi R2, R0, 99
          sw lit_t4(R0), R2
          lw R1, lit_t4(R0)
          jl R15, putint
          j end
x1        nop
          addi R2, R0, 77
          sw lit_t5(R0), R2
          lw R1, lit_t5(R0)
          jl R15, putint
end       nop
          hlt

% PROGRAM END

% space for constant 0
lit_t1 res 4
lit_t2 res 4
lit_t3 res 4
lit_t4 res 4
lit_t5 res 4
