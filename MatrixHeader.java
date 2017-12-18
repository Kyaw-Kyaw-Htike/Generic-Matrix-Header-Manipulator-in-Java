// Copyright (C) 2017 Kyaw Kyaw Htike @ Ali Abdul Ghafur. All rights reserved.

package KKH.Matrix;

public class MatrixHeader {
    // nrows of the original matrix
    private int nr;
    // ncols of the original matrix
    private int nc;
    // nchannels of the original matrix
    private int nch;
    // num data of the original matrix, i.e. nrows * ncols * nchannels
    private int ndata;
    // submatrix type_indicator: 1: not submatrix (i.e. full original matrix);
    // 2: continuous submatrix; 3: discontinuous submatrix
    private int submatType_id;
    // applicable only if isSubmatrix && isContView are true. Starting row (inclusive)
    private int r1;
    // applicable only if isSubmatrix && isContView are true. Ending row (inclusive)
    private int r2;
    // applicable only if isSubmatrix && isContView are true. Starting col (inclusive)
    private int c1;
    // applicable only if isSubmatrix && isContView are true. Ending col (inclusive)
    private int c2;
    // applicable only if isSubmatrix && isContView are true. Starting channel (inclusive)
    private int ch1;
    // applicable only if isSubmatrix && isContView are true. Ending channel (inclusive)
    private int ch2;

    private int r1_req_prev;
    private int c1_req_prev;
    private int ch1_req_prev;

    // the following are for non-continuous views
    private int[] row_indices;
    private int[] col_indices;
    private int[] channel_indices;

    public MatrixHeader()
    {
        nr = nc = nch = ndata = submatType_id = r1 = r2 = c1 = c2 = ch1 = ch2 = 0;
    }

    public MatrixHeader(int nrows, int ncols, int nchannels)
    {
        nr = nrows;
        nc = ncols;
        nch = nchannels;
        ndata = nrows * ncols * nchannels;
        submatType_id = 1; // to indicate full (original) matrix
        r1 = 0;
        r2 = nrows - 1;
        c1 = 0;
        c2 = ncols - 1;
        ch1 = 0;
        ch2 = nchannels - 1;
        r1_req_prev = 0;
        c1_req_prev = 0;
        ch1_req_prev = 0;
    }

    // deep copy of the current matrix header
    public MatrixHeader clone()
    {
        MatrixHeader h = new MatrixHeader();
        h.nr = nr;
        h.nc = nc;
        h.nch = nch;
        h.ndata = ndata;
        h.submatType_id = submatType_id;
        h.r1 = r1;
        h.r2 = r2;
        h.c1 = c1;
        h.c2 = c2;
        h.ch1 = ch1;
        h.ch2 = ch2;

        h.row_indices = new int[row_indices.length];
        h.col_indices = new int[col_indices.length];
        h.channel_indices = new int[channel_indices.length];

        for(int i=0; i<row_indices.length; i++)
            h.row_indices[i] = row_indices[i];
        for(int i=0; i<col_indices.length; i++)
            h.col_indices[i] = col_indices[i];
        for(int i=0; i<channel_indices.length; i++)
            h.channel_indices[i] = channel_indices[i];

        return h;
    }

    public int nrows()
    {
        System.out.println("The submatType_id = " + submatType_id);
        switch(submatType_id)
        {
            case 1:
                System.out.println("Inside nrows(), return nr = " + nr);
                return nr;
            case 2:
                System.out.println("Inside nrows(), return r1-r2+1 = " + (r2-r1+1));
                return r2-r1+1;
            case 3:
                return row_indices.length;
            default:
                return 0;
        }
    }

    public int ncols()
    {
        System.out.println("The submatType_id = " + submatType_id);
        switch(submatType_id)
        {
            case 1:
                return nc;
            case 2:
                return c2-c1+1;
            case 3:
                return col_indices.length;
            default:
                return 0;
        }
    }

    public int nchannels()
    {
        System.out.println("The submatType_id = " + submatType_id);
        switch(submatType_id)
        {
            case 1:
                return nch;
            case 2:
                return ch2-ch1+1;
            case 3:
                return channel_indices.length;
            default:
                return 0;
        }
    }

    // transform from submatrix i,j,k position space to original matrix position space
    public int i(int i)
    {
        switch(submatType_id)
        {
            case 1:
                return i;
            case 2:
                return i + r1;
            case 3:
                return row_indices[i];
            default:
                System.out.println("ERROR: Invalid index");
                return -1;
        }
    }

    // transform from submatrix i,j,k position space to original matrix position space
    public int j(int j)
    {
        switch(submatType_id)
        {
            case 1:
                return j;
            case 2:
                return j + c1;
            case 3:
                return col_indices[j];
            default:
                System.out.println("ERROR: Invalid index");
                return -1;
        }
    }

    // transform from submatrix i,j,k position space to original matrix position space
    public int k(int k)
    {
        switch(submatType_id)
        {
            case 1:
                return k;
            case 2:
                return k + ch1;
            case 3:
                return channel_indices[k];
            default:
                System.out.println("ERROR: Invalid index");
                return -1;
        }
    }

    // take a continuous submatrix of either the original matrix
    // or the current submatrix
    public void submat(int r1, int r2, int c1, int c2, int ch1, int ch2)
    {

        System.out.println("Beginning ch2 = " + ch2);
        if(submatType_id == 3)
        {
            System.out.println("ERROR: cannot take a continuous submatrix of a discontinuous matrix.");
            return;
        }

        // special notation of -1 to mean the last element
        if(r1 == -1) r1 = this.r2;
        if(r2 == -1) r2 = this.r2;
        if(c1 == -1) c1 = this.c2;
        if(c2 == -1) c2 = this.c2;
        if(ch1 == -1) ch1 = this.ch2;
        if(ch2 == -1) ch2 = this.ch2;

        System.out.println("Now ch2 = " + ch2);

        int r1_prev = this.r1;
        int c1_prev = this.c1;
        int ch1_prev = this.ch1;

        // move the positions
        this.r1 = r1 + r1_prev - r1_req_prev;
        this.r2 = r2 + r1_prev - r1_req_prev;
        this.c1 = c1 + c1_prev - c1_req_prev;
        this.c2 = c2 + c1_prev - c1_req_prev;
        this.ch1 = ch1 + ch1_prev - ch1_req_prev;
        this.ch2 = ch2 + ch1_prev - ch1_req_prev;

        r1_req_prev = r1;
        c1_req_prev = c1;
        ch1_req_prev = ch1;

        System.out.println("New r1 = " + this.r1);
        System.out.println("New r2 = " + this.r2);
        System.out.println("New c1 = " + this.c1);
        System.out.println("New c2 = " + this.c2);
        System.out.println("New ch1 = " + this.ch1);
        System.out.println("New ch2 = " + this.ch2);

        submatType_id = 2;

    }

    // take a discontinuous submatrix of either the original (full) matrix
    // or a continuous submatrix
    public void submat(int[] row_indices, int[] col_indices, int[] channel_indices)
    {
        if(submatType_id == 3)
        {
            System.out.println("ERROR: cannot take a discontinuous submatrix of an already discontinuous submatrix.");
            return;
        }

        submatType_id = 3;

        this.row_indices = new int[row_indices.length];
        this.col_indices = new int[col_indices.length];
        this.channel_indices = new int[channel_indices.length];

        for(int i=0; i<row_indices.length; i++)
            this.row_indices[i] = row_indices[i] + r1;
        for(int i=0; i<col_indices.length; i++)
            this.col_indices[i] = col_indices[i] + c1;
        for(int i=0; i<channel_indices.length; i++)
            this.channel_indices[i] = channel_indices[i] + ch1;
    }

    // take a continuous submatrix in the form of a row
    public void row(int row_index)
    {
        submat(row_index, row_index, c1, c2, ch1, ch2);
    }

    // take a continuous submatrix in the form of rows
    public void rows(int start_index, int end_index)
    {
        submat(start_index, end_index, c1, c2, ch1, ch2);
    }

    // take a discontinuous submatrix in the form of rows
    public void rows(int[] row_indices_)
    {
        int[] col_indices_ = new int[c2-c1+1];
        for(int i=c1, j=0; i<=c2; i++, j++)
            col_indices_[j] = i;

        int[] channel_indices_ = new int[ch2-ch1+1];
        for(int i=ch1, j=0; i<=ch2; i++, j++)
            channel_indices_[j] = i;

        submat(row_indices_, col_indices_, channel_indices_);
    }

    // take a continuous submatrix in the form of a column
    public void col(int col_index)
    {
        submat(r1, r2, col_index, col_index, ch1, ch2);
    }

    // take a continuous submatrix in the form of cols
    public void cols(int start_index, int end_index)
    {
        submat(r1, r2, start_index, end_index, ch1, ch2);
    }

    // take a discontinuous submatrix in the form of cols
    public void cols(int[] col_indices_)
    {
        int[] row_indices_ = new int[r2-r1+1];
        for(int i=r1, j=0; i<=r2; i++, j++)
            row_indices_[j] = i;

        int[] channel_indices_ = new int[ch2-ch1+1];
        for(int i=ch1, j=0; i<=ch2; i++, j++)
            channel_indices_[j] = i;

        submat(row_indices_, col_indices_, channel_indices_);
    }

    // take a continuous submatrix in the form of a channel
    public void channel(int channel_index)
    {
        submat(r1, r2, c1, c2, channel_index, channel_index);
    }

    // take a continuous submatrix in the form of channels
    public void channels(int start_index, int end_index)
    {
        submat(r1, r2, c1, c2, start_index, end_index);
    }

    // take a discontinuous submatrix in the form of cols
    public void channels(int[] channel_indices_)
    {
        int[] row_indices_ = new int[r2-r1+1];
        for(int i=r1, j=0; i<=r2; i++, j++)
            row_indices_[j] = i;

        int[] col_indices_ = new int[c2-c1+1];
        for(int i=c1, j=0; i<=c2; i++, j++)
            col_indices_[j] = i;

        submat(row_indices_, col_indices_, channel_indices_);
    }

}
