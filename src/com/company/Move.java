package com.company;

public class Move 
{
	// consists of a combination of 3 letters representing 3 coins (H or T)
    private String fromCoins;
	private String toCoins;
    private int coinIndex;
    private char coinChar;
    private boolean legal; //is it a legal move or not
    private char[] coins;
    
    
    public Move(String strFrom, String strTo, int coinNum, char coinType)
    {
    	fromCoins = strFrom;
    	toCoins = strTo;
    	coinIndex = coinNum;
    	coinChar = coinType;
    	coins = new char[3];
    	for(int x = 0; x < 3; x++)
    	{
    		coins[x] = toCoins.charAt(x);
    	}
    	
    	legal = isLegal();
    	
    }
    
    
    public boolean isLegal()
    {
    	boolean isLegal = false;
    	
    	switch(coinIndex)
    	{
    		case 0:
    			if(coinChar == 'H' && coins[1] == 'T' && coins[2] == 'T')
    				isLegal = true;
    			else if(coinChar == 'T' && coins[1] == 'H' && coins[2] == 'H')
    				isLegal = true;
    			else if(coinChar == 'H' && coins[1] == 'H' && coins[2] == 'H')
    				isLegal = true;
    			else if(coinChar == 'T' && coins[1] == 'T' && coins[2] == 'T')
    				isLegal = true;
    			else
    				isLegal = false;
    			break;
    		case 2:
    			if(coinChar == 'H' && coins[0] == 'T' && coins[1] == 'T')
    				isLegal = true;
    			else if(coinChar == 'T' && coins[0] == 'H' && coins[1] == 'H')
    				isLegal = true;
    			else if(coinChar == 'H' && coins[0] == 'H' && coins[1] == 'H')
    				isLegal = true;
    			else if(coinChar == 'T' && coins[0] == 'T' && coins[1] == 'T')
    				isLegal = true;
    			else
    				isLegal = false;
    			break;
    		default:
    			isLegal = true;
    			break;
    	}
    	
    	return isLegal;
    }

    
    
    public boolean getLegal()
    {
    	return legal;
    }
	
	
}
