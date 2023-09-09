package main
 
import (
	"encoding/json"
	"io/ioutil"
	"strconv"
	"fmt"
)
 
type BitcoinTxNode struct {
	Key  string `json:"_key"`
	Time int64  `json:"time"`
}

type BitcoinBlockNode struct {
	BlockHeight uint64 `json:"blockHeight"`
	Key         string `json:"_key"`
	BlockHash   string `json:"blockHash"`
}

type BitcoinParentBlockEdge struct {
	Key  string `json:"_key"`
	From string `json:"_from"`
	To   string `json:"_to"`
}

type BitcoinNextEdge struct {
	Key     string `json:"_key"`  // arango _key is generated by arango  txId + '_' + outIndex
	From    string `json:"_from"` // arango id of transaction 'btcTx/{_key}'
	To      string `json:"_to"`   // arango id of block 'btcTx/{_key}'
	Address string `json:"address"`
	//OutIndex	int 	`json:"outIndex`
	SpentBtc uint64 `json:"spentBtc"`
}

func main() {
	var n, t, nb, ntx uint64
	fmt.Println("Enter the amount of blocks: ")
	fmt.Scanf("%d", &nb)
	fmt.Println("Enter the amount of txs: ")
	fmt.Scanf("%d", &ntx)

	blocks := make([]BitcoinBlockNode, 0, 100)
	txs := make([]BitcoinTxNode, 0, 1000)
	next := make([]BitcoinNextEdge, 0, 1000)
	parents := make([]BitcoinParentBlockEdge, 0, 1000)

	for n = 1; n <= nb; n++ {
		bl := BitcoinBlockNode{
			BlockHeight: n,
			Key:         "key_block_" + strconv.FormatInt(int64(n), 10),
			BlockHash:   strconv.FormatInt(int64(n), 10),
		}
		blocks = append(blocks, bl)

		/* get all txid from msg_block - block.Tx */
		/* for each txid get the raw transaction */
		firsttx := BitcoinTxNode{
			Key:  "key_tx_0_Ofblock_" + strconv.FormatInt(int64(n), 10),
			Time: 1111,
		}
		txs = append(txs, firsttx)
		for t = 1; t <= ntx/nb; t++ {
			tx := BitcoinTxNode{
				Key:  "key_tx_" + strconv.FormatInt(int64(t), 10) + "_Ofblock_" + strconv.FormatInt(int64(n), 10),
				Time: 1234,
			}
			txs = append(txs, tx)
			parentBlockKey := "Toblock_" + strconv.FormatInt(int64(n), 10) + "_Fromtx_" + strconv.FormatInt(int64(t), 10)
			parents = append(parents, BitcoinParentBlockEdge{
				Key:  "key_" + parentBlockKey,
				From: "btcTx/key_tx_" + strconv.FormatInt(int64(t), 10) + "_Ofblock_" + strconv.FormatInt(int64(n), 10),
				To:   "btcBlock/key_block_" + strconv.FormatInt(int64(n), 10),
			})
			nexte := BitcoinNextEdge{
				Key:      "key_next_tx_from_" + strconv.FormatInt(int64(t-1), 10) + "_Ofblock_" + strconv.FormatInt(int64(n), 10) + "_to_" + strconv.FormatInt(int64(t), 10) + "_Ofblock_" + strconv.FormatInt(int64(n), 10),
				From:     "btcTx/key_tx_" + strconv.FormatInt(int64(t-1), 10) + "_Ofblock_" + strconv.FormatInt(int64(n), 10),
				To:       "btcTx/key_tx_" + strconv.FormatInt(int64(t), 10) + "_Ofblock_" + strconv.FormatInt(int64(n), 10),
				Address:  "someaddrhex",
				SpentBtc: 20,
			}
			next = append(next, nexte)
			if t % 10 == 0 && t != 100 {
				nexte := BitcoinNextEdge{
					Key:      "key_next_tx_from_" + strconv.FormatInt(int64(t), 10) + "_Ofblock_" + strconv.FormatInt(int64(n), 10) + "_to_" + strconv.FormatInt(int64(0), 10) + "_Ofblock_" + strconv.FormatInt(int64(n+1), 10),
					From:     "btcTx/key_tx_" + strconv.FormatInt(int64(t), 10) + "_Ofblock_" + strconv.FormatInt(int64(n), 10),
					To:       "btcTx/key_tx_0_Ofblock_" + strconv.FormatInt(int64(n+1), 10),
					Address:  "someaddrhex",
					SpentBtc: 20,
				}
				next = append(next, nexte)
			}
		}
	}

	
 
	file1, _ := json.MarshalIndent(blocks, "", " ")
	file2, _ := json.MarshalIndent(txs, "", " ")
	file3, _ := json.MarshalIndent(parents, "", " ")
	file4, _ := json.MarshalIndent(next, "", " ")
 
	_ = ioutil.WriteFile("testdata/btcBlock.json", file1, 0644)
	_ = ioutil.WriteFile("testdata/btcTx.json", file2, 0644)
	_ = ioutil.WriteFile("testdata/btcParentBlock.json", file3, 0644)
	_ = ioutil.WriteFile("testdata/btcNext.json", file4, 0644)
}