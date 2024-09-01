package data.source.seachbook

import io.kotest.matchers.nulls.shouldNotBeNull
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpHeaders
import io.ktor.http.headersOf
import io.ktor.utils.io.ByteReadChannel
import kotlinx.coroutines.runBlocking
import kotlin.test.Test

val response = """
            {
            	"kind": "books#volumes",
            	"totalItems": 409,
            	"items": [
            		{
            			"kind": "books#volume",
            			"id": "riXZDwAAQBAJ",
            			"etag": "3n4xL15iD4s",
            			"selfLink": "https://www.googleapis.com/books/v1/volumes/riXZDwAAQBAJ",
            			"volumeInfo": {
            				"title": "情緒跟你以為的不一樣──科學證據揭露喜怒哀樂如何生成",
            				"authors": [
            					"麗莎．費德曼．巴瑞特博士(Lisa Feldman Barrett, Ph.D.)"
            				],
            				"publisher": "商周出版",
            				"publishedDate": "2020-03-03",
            				"description": "情緒科學的思想革命之作！ 國際重量級心理學家暨神經科學家代表作 了解情緒，就是了解我們自己 情緒並非自發產生，而是由你的大腦建構出來的文化類別 根據傳統情緒觀點，情緒是自發的，所以科學家長期以來認為情緒在身體或大腦中是固定線路，由大腦的固定部位決定，比如傳統情緒觀點就認為杏仁核掌管恐懼，缺乏杏仁核就無法習得恐懼。本書所提出的情緒建構理論，就以各種實驗數據和科學證據來顛覆傳統情緒觀點。 本書作者麗莎．費德曼．巴瑞特博士，曾獲得美國國家衛生研究院先鋒獎和2019年古根漢研究學者獎（每年全球僅二人次），是國際知名的心理學家暨神經科學家。她在本書中以實驗室的縝密研究數據，佐以諸多其他科學的研究證據，說明大腦如何構建我們的情緒，情緒實際上並非生來就有，也非普世共通，而是你所身處的社會共識、文化與教養下的產物。情緒建構論，是一個能徹底改變心理學、醫療保健、法律體系以及我們對人類心靈理解的情緒新理論，本書更是情緒科學的典範轉移之作。 巴瑞特博士在本書中以各種淺顯易懂的例子說明大腦如何建構情緒，更提出喜怒哀樂等種種情緒，實際上只是大腦建構的文化類別和概念，就好比，杯子蛋糕和瑪芬，因為食用時機不同，早餐吃的叫瑪芬，點心時間吃的叫杯子蛋糕。以情緒來說，你感到胃糾成一團，如果你現在是在醫院等檢查報告，你可能會把胃痛當成是「焦慮」的生理現象，但如果你目前是在餐桌前，可能你就會把胃痛當成「肚子餓」的生理現象；或者，你原本覺得某個人並非你可以交往的對象，但今天跟他碰面時，你卻突然感到心跳加速、面頰泛紅，你不禁懷疑，難道原本對這個人的認知錯了嗎？晚上卻發現自己得了流感！（這是巴瑞特博士的親身經歷。）這樣的例子不勝枚舉，你還能說「情緒是自發、與生俱來」的嗎？ 巴瑞特博士更以情緒建構論為基礎，提供可以掌控自己情緒的方法。因為你的身體和你的心智深深地相互連結，你的內感驅動你的行為，你的文化又串連你的大腦。所以事實上，掌控情緒的最基本功夫，就是保持身體預算的良好狀態。因此，健康的食物、規律的運動和睡眠，是身體預算平衡與情緒生活健康的先決條件，她據此建議了提升身體健康以及情商健康的方法。提升身體預算能力的方法，包括能提升與人接觸的按摩、慢呼吸的瑜珈、布置綠色的安靜環境、引人入勝的小說……甚至定期跟朋友聚餐，並且輪流請客，都有益於身體健康。至於提高情商的方法，重點則在於增加情緒粒度和概念，更有效地預測和分類你的感覺，這樣更能依照環境制訂你的行為；根據神經科學，學習新語詞也能提高情緒健康……。 巴瑞特博士的情緒建構理論正在推動對思想和大腦的更深層次理解。《情緒跟你以為的不一樣》不僅揭露了情緒、思維和大腦科學的最新研究，更以許多實例闡明有趣的實際應用。 〈好評推薦與讚譽〉 輔仁大學心理學系副教授 黃揚名 審定｜導讀 泛科知識公司知識長 鄭國威 國立臺灣大學心理學系副教授 謝伯讓 專文推薦 中央硏究院民族所兼任硏究員暨國立臺灣大學心理學系兼任教授 朱瑞玲 心曦心理諮商所心理師 周慕姿 揚生慈善基金會執行長 許華倚 台灣愛笑瑜伽協會創會會長 陳達誠 好評推薦 我蠻期待大家可以認真讀《情緒跟你以為的不一樣》這本書，因為這真的不只是一本想要談情緒是怎麼來的一本書，巴瑞特教授更想帶大家去思考，你的思維是怎麼來的，大腦又透過什麼樣的方式來達成這個艱鉅的任務。我沒辦法逼大家相信巴瑞特教授的理論，但我蠻希望大家用開放的態度來認識情緒建構論，我相信你必定能從她深入淺出的引導中，透過認識情緒的真諦，找到自己人生的一些方向。──輔仁大學心理學系副教授 黃揚名 就如同電影故事總會迎來結局，也得有人揭露情緒的科學真相。本書作者，心理學與神經科學家巴瑞特便勇敢地扛起了這份重責大任，使本書成為心理學與神經科學，甚至是所有知識領域，都不能錯過的典範轉移之作。──泛科知識公司知識長 鄭國威 從某種思想革命的角度來看，巴瑞特的《情緒跟你以為的不一樣》或許有機會比擬達爾文的《物種起源》。……在《情緒跟你以為的不一樣》這場引人入勝的情緒科學之旅中，你將會見到新理論與新證據的精彩交織。巴瑞特教授所提出的嶄新世界觀，將會撼動你我原本自以為是的情緒與經驗世界！──國立臺灣大學心理學系暨研究所副教授 謝伯讓 關於情緒，東方一向以較虛無縹緲的方式來表達，如莊子的「咸其自取」，如禪學正念的「無揀擇」!西方則較結構、邏輯與科學，這本書將帶你從全新的科學角度來看不科學的情緒，讓我們見樹又見林，照顧好自己的自癒力！──揚生慈善基金會執行長 許華倚 太棒了！此書剛好印證我最近幾年鼓勵笑友要「笑出智慧」比「笑出健康快樂」更重要，「智慧」就是覺察細微的身體表徵感受，減少習性反應，掌握製造情緒的主權。──台灣愛笑瑜伽協會創會會長 陳達誠 創新的想法造就出非凡卓越、獨一無二的著作，以大膽而清晰的方式展現在大眾面前。──《科學人》（Scientific American） 真是太美妙了……巴瑞特用令人信服的例子和故事清楚交代來龍去脈，幫助我們深入了解情緒研究領域的多數重大發展……這是一場發人深省的探索之旅。──《華爾街日報》（Wall Street Journal） 我們活在世上，多數人都不曾認真想過自己帶著什麼面對生活的一切。麗莎．費德曼．巴瑞特不但思考而且仔細探討，她對我們的知覺和情緒所做的描述，真是令人驚嘆不已。──《ELLE》 科學佐證的發現帶來驚喜連連……內容有趣直叫人不忍釋手。──《富比士》（FORBES） 我們全都對情緒懷有直覺：你自動地經驗喜悅、恐懼或生氣的方式，幾乎就跟喀拉哈里沙漠的狩獵採集者完全相同。在這本絕妙的新書中，麗莎．巴瑞特選用當代的研究讓我們看見截然不同的畫面：情緒經驗相當個人化，而且在神經生物學上獨樹一格，不可能跟認知完全分開。這本容易上手的好書，帶給我們滿滿的重要訊息還有刺激挑戰。──《為什麼斑馬不會得胃潰瘍？》（Why Zebras Don’t Get Ulcer）及《一隻靈長類的回憶錄》（A Primate’s Memoir）作者 羅伯特．薩波斯基（Robert Sapolsky） 讀完《情緒跟你以為的不一樣》後，我開始用全新的眼光看待情緒。麗莎．巴瑞特開啟了一個嶄新天地，讓我們能以不同角度對抗性別刻板印象以及制訂更好的政策。──《未竟之業》（Unfinished Business）作者 安－瑪莉．史勞特（Anne-Marie Slaughter） 《情緒跟你以為的不一樣》帶給我們一個全新的情緒概念：情緒是什麼、它們來自哪裡，以及（最重要的）它們不是什麼。腦科學是一門違反直覺的學科，而麗莎．巴瑞特的超凡能力成功地將違反直覺變得可以理解。這本書會讓你忍不住地拍案叫絕，納悶為什麼過了這麼久才這樣思考大腦。──《失敗：科學的成功之道》（Failure: Why Science Is So Successful）及《無知：它怎樣驅動科學》（Ignorance: How It Drives Science）作者 司徒．法爾斯坦（Stuart Firestein） 曾經好奇你的情緒來自哪裡嗎？情緒心理學的世界級專家麗莎．巴瑞特，為感受與其背後的神經科學撰寫了一部最完整可靠的指南。──暢銷書《恆毅力》（Grit）作者 安琪拉．達克沃斯（Angela Duckworth） 《情緒跟你以為的不一樣》細細分析我們的腦如何精彩地創造我們的情緒生活，這本見解深刻、挑動人心而且充滿魅力的書，毫不費力地將最先進的神經科學研究連上日常的情緒。在你讀完這本重要的書之後，你將不會再以相同的方式考慮情緒。──《記憶七罪》（The Seven Sins of Memory）作者 丹尼爾．沙克特（Daniel L. Schacter） 如果你認為自己對慾念、憤怒、悲痛和喜悅的一切所知都是錯的該怎麼辦呢？麗莎．巴瑞特是心理學界中最睿智、也最有創造力的科學家之一，她提出的情緒建構理論真的相當基本而且迷人。透過活靈活現的例子加上清晰銳利的文筆，《情緒跟你以為的不一樣》抗辯了人類天性最核心面向的大膽新視野。──《失控的同理心》（Against Empathy）和《香醇的紅酒比較貴，還是昂貴的紅酒比較香》（How Pleasure Works）作者 保羅．布倫（Paul Bloom） 你以為你對自己有何感受與為何感受所知道的一切，結果證明錯誤得離譜。麗莎．巴瑞特闡明的全新情緒科學著實令人著迷，她也提供現實例子解釋這在健康、教養、戀愛關係，甚至是國家安全等各個領域為何如此重要。──《女孩與性》（Girls & Sex）作者 佩吉．奧倫斯坦（Peggy Orenstein） 這本經過透徹研究和深思熟慮的縝密著作，讓我們有機會一窺關於情緒的最新洞察：情緒是什麼、它們來自哪裡，以及為什麼我們擁有它們。任何糾結於如何調解心與腦的人，都該把這本書視為珍寶，它在沒有縮減這個主題的人本主義之下清楚解釋了科學。──暢銷書《背離親緣》（Far from the Tree）和《正午惡魔》（The Noonday Demon）作者 安德魯．所羅門（Andrew Solomon） 麗莎．巴瑞特巧妙地整合情感科學、神經科學、社會心理學和哲學的發現，使我們能理解每一天所經驗和見證的許多情緒實例。《情緒跟你以為的不一樣》將幫助你改造自己的生活，並且賦予你新的眼光重新看待熟悉的感受──無論是焦慮或愛等等。──《正向性》（Positivity）和《愛是正能量，不練習，會消失！》（Love 2.0）作者 芭芭拉．佛列德里克森（Barbara Fredrickson） 麗莎．巴瑞特非常清楚地寫道，你的情緒不僅僅跟你生下來帶著什麼有關，還跟你的大腦如何把感受拼湊在一起有關，她也詳細解釋你如何能促成這個過程。她所說的這個故事，相當具有說服力。──《焦慮》（Anxious）和《突觸的字我》（Synaptic Self）作者 約瑟夫．李竇（Joseph LeDoux） 這是一本精采絕倫的情緒科學書，出自繼達爾文以來最深入思考這個主題的專家原創。──暢銷書《快樂為什麼不幸福？》（Stumbling on Happiness）作者 丹尼爾．吉伯特（Daniel Gilbert） 《情緒跟你以為的不一樣》是在了解我們如何知覺、判斷和決定的探索中，一本最棒的傑作。這本書為解決人類行為的許多謎題打下基礎。我期盼這種更準確的情緒觀點，能對我的運動和商業個案有所幫助。──ReThink Group創辦人暨CEO 丹妮絲．舒爾（Denise K. Shull） 透過《情緒跟你以為的不一樣》一書，麗莎．巴瑞特為21世紀的情緒理論設下了辯論條件。她用清楚、易讀的筆觸，邀請我們探詢情緒是什麼的一般和專業理解，她還集結超大量的資料用以提出新的解答。巴瑞特關於人類如何建構情緒的理論對法律有重大影響，其中包括冷靜法官的迷思。她提出的「法制系統的情感科學宣言」，值得理論家和從業者同樣地認真對待。──范德堡大學（Vanderbilt University）法律系教授暨醫療、健康與社會系教授 泰瑞．馬洛尼（Terry Maroney） 進行重大刑事審判的每一位律師和法官，都應該要仔細地閱讀這本書。我們全都一直努力應對自由意志、情緒衝動和犯罪意圖的概念，然而這些主題在本書中都面臨新的檢視，舊的假設更是一一受到挑戰。腦科學與法律之間的介面，突然之間成為我們應該辯論的範圍。──英國上議院御用大律師 海倫娜．肯尼迪男爵夫人（Baroness Helena Kennedy） 書中超凡卓越的書寫、邏輯和學識，讓人忍不住心生讚賞，即使是那些對建構主義挑戰簡單版的基本情緒理論都感到驚惶失措的人也如此。──《我們為什麼生病》（Why We Get Sick）作者 蘭多夫．內斯（Randolph Nesse） 準備好轉轉你的腦筋，跟著心理學教授巴瑞特來一場動腦之旅……她對這個主題的熱情，點亮了關於我們的情緒來自哪裡的每個理論和令人驚嘆的事實。小提示：跟你想的不一樣。確實，每一章都滿滿寫著令人驚艷的洞察……巴瑞特的大腦自拍真是讓人眼睛為之一亮。──《書目期刊》（Booklist），重點書評 關於情緒和理性相互爭執的普遍觀點，書中有著清楚表述的有趣辯論……巴瑞特指出，這點具有重要的法律和道德意涵，牽引出圍繞自由意志的棘手問題。從這資訊豐富、範圍廣泛而且易讀的討論中，我們獲悉「心理學、神經科學和相關的學科如何不再尋找情緒指紋，轉而詢問情緒如何建構出來。──《科克斯書評》（Kirkus Review），重點書評 巴瑞特……提出違反直覺的理論，對抗的不只是大眾的了解，還有傳統的研究：情緒不會出現，而是我們飛快地建構它們……從傳統情緒觀點出發，巴瑞特逐步建立起自己的理論，她以對話的語氣書寫，並且使用輕鬆實際的比喻，另把最沉重的神經科學整理成附錄，好讓整本書能保持順暢易讀。──《圖書館期刊》（Library Journal），重點書評 這本書寫得真是太棒了！麗莎．巴瑞特的《情緒跟你以為的不一樣》可說是在情緒科學史上的一場典範轉移。不單只是史上記錄，本書還是絕佳的翻譯作品，它將新的情緒神經科學翻譯成可以理解而且好讀的詞彙。由於這門科學在警察槍擊案和TSA研判風險可能性等迥異的各領域中都有深遠的意涵，這個翻譯對於科學家和市民、立法者和醫生等等都相當重要。（舉例來說，如果預謀殺人〔理性思考的產物，我們認為罪責最重〕和較輕微的過失殺人〔「激情犯罪」〕之間少了有意義的科學差異該怎麼辦呢？）情緒不是常駐在專屬的大腦部位，不斷地跟負責認知或知覺的部位交戰，像是皮克斯的電影《腦筋急轉彎》的誇張表現，更別說是笛卡兒、柏拉圖或其他哲學家所描述的大腦。大腦也不是被動地從「外界」」擷取資料來對它反應。大腦利用核心大腦系統而不是特化的迴路，建構它知覺的真實，以及它（和我們）經驗的情緒。而且在所屬的文化中，跟其他的大腦一起這麼做。這個研究（「只」挑戰關於大腦的兩千歲假設）的意涵和抱負，簡直可說是令人嘆為觀止。更讓人驚訝的是，它的成品是如此地完美出色。──哈佛法學院資深講師暨前美國麻州聯邦地方法院法官 南希．葛特納（Nancy Gertner） 出版社 商周出版 (城邦)",
            				"readingModes": {
            					"text": true,
            					"image": true
            				},
            				"maturityRating": "NOT_MATURE",
            				"allowAnonLogging": true,
            				"contentVersion": "1.3.4.0.preview.3",
            				"panelizationSummary": {
            					"containsEpubBubbles": false,
            					"containsImageBubbles": false
            				},
            				"imageLinks": {
            					"smallThumbnail": "http://books.google.com/books/content?id=riXZDwAAQBAJ&printsec=frontcover&img=1&zoom=5&edge=curl&source=gbs_api",
            					"thumbnail": "http://books.google.com/books/content?id=riXZDwAAQBAJ&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api"
            				},
            				"previewLink": "http://books.google.com.tw/books?id=riXZDwAAQBAJ&printsec=frontcover&dq=%E6%83%85%E7%B7%92&hl=&cd=1&source=gbs_api",
            				"infoLink": "https://play.google.com/store/books/details?id=riXZDwAAQBAJ&source=gbs_api",
            				"canonicalVolumeLink": "https://play.google.com/store/books/details?id=riXZDwAAQBAJ"
            			},
            			"saleInfo": {
            				"country": "TW",
            				"listPrice": {
            					"amount": 340,
            					"currencyCode": "TWD"
            				},
            				"retailPrice": {
            					"amount": 231,
            					"currencyCode": "TWD"
            				},
            				"buyLink": "https://play.google.com/store/books/details?id=riXZDwAAQBAJ&rdid=book-riXZDwAAQBAJ&rdot=1&source=gbs_api",
            				"offers": [
            					{
            						"finskyOfferType": 1,
            						"listPrice": {
            							"amountInMicros": 340000000,
            							"currencyCode": "TWD"
            						},
            						"retailPrice": {
            							"amountInMicros": 231000000,
            							"currencyCode": "TWD"
            						}
            					}
            				]
            			},
            			"accessInfo": {
            				"country": "TW",
            				"epub": {
            					"isAvailable": true,
            					"acsTokenLink": "http://books.google.com.tw/books/download/%E6%83%85%E7%B7%92%E8%B7%9F%E4%BD%A0%E4%BB%A5%E7%82%BA%E7%9A%84%E4%B8%8D%E4%B8%80%E6%A8%A3_%E7%A7%91%E5%AD%B8%E8%AD%89-sample-epub.acsm?id=riXZDwAAQBAJ&format=epub&output=acs4_fulfillment_token&dl_type=sample&source=gbs_api"
            				},
            				"pdf": {
            					"isAvailable": true,
            					"acsTokenLink": "http://books.google.com.tw/books/download/%E6%83%85%E7%B7%92%E8%B7%9F%E4%BD%A0%E4%BB%A5%E7%82%BA%E7%9A%84%E4%B8%8D%E4%B8%80%E6%A8%A3_%E7%A7%91%E5%AD%B8%E8%AD%89-sample-pdf.acsm?id=riXZDwAAQBAJ&format=pdf&output=acs4_fulfillment_token&dl_type=sample&source=gbs_api"
            				},
            				"accessViewStatus": "SAMPLE"
            			},
            			"searchInfo": {
            				"textSnippet": "情緒科學的思想革命之作！ 國際重量級心理學家暨神經科學家代表作 了解情緒，就是了解我們自己 情緒並非自發產生，而是由你的大腦建構出來的文化類別 ..."
            			}
            		},
            		{
            			"kind": "books#volume",
            			"id": "nU3H9-ZXv5oC",
            			"etag": "XnaIUanw5+c",
            			"selfLink": "https://www.googleapis.com/books/v1/volumes/nU3H9-ZXv5oC",
            			"volumeInfo": {
            				"title": "感性與理性",
            				"subtitle": "了解我們的情緒",
            				"authors": [
            					"Lazarus, Richard S.·拉扎勒斯",
            					"Lazarus, Bernice N.·拉扎勒斯",
            					"李素卿"
            				],
            				"publisher": "五南圖書出版股份有限公司",
            				"publishedDate": "2002",
            				"readingModes": {
            					"text": false,
            					"image": true
            				},
            				"maturityRating": "NOT_MATURE",
            				"allowAnonLogging": false,
            				"contentVersion": "0.9.2.0.preview.1",
            				"panelizationSummary": {
            					"containsEpubBubbles": false,
            					"containsImageBubbles": false
            				},
            				"imageLinks": {
            					"smallThumbnail": "http://books.google.com/books/content?id=nU3H9-ZXv5oC&printsec=frontcover&img=1&zoom=5&edge=curl&source=gbs_api",
            					"thumbnail": "http://books.google.com/books/content?id=nU3H9-ZXv5oC&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api"
            				},
            				"previewLink": "http://books.google.com.tw/books?id=nU3H9-ZXv5oC&printsec=frontcover&dq=%E6%83%85%E7%B7%92&hl=&cd=2&source=gbs_api",
            				"infoLink": "http://books.google.com.tw/books?id=nU3H9-ZXv5oC&dq=%E6%83%85%E7%B7%92&hl=&source=gbs_api",
            				"canonicalVolumeLink": "https://books.google.com/books/about/%E6%84%9F%E6%80%A7%E8%88%87%E7%90%86%E6%80%A7.html?hl=&id=nU3H9-ZXv5oC"
            			},
            			"saleInfo": {
            				"country": "TW"
            			},
            			"accessInfo": {
            				"country": "TW",
            				"epub": {
            					"isAvailable": false
            				},
            				"pdf": {
            					"isAvailable": true,
            					"acsTokenLink": "http://books.google.com.tw/books/download/%E6%84%9F%E6%80%A7%E8%88%87%E7%90%86%E6%80%A7-sample-pdf.acsm?id=nU3H9-ZXv5oC&format=pdf&output=acs4_fulfillment_token&dl_type=sample&source=gbs_api"
            				},
            				"accessViewStatus": "SAMPLE"
            			}
            		},
            		{
            			"kind": "books#volume",
            			"id": "ItuaDwAAQBAJ",
            			"etag": "ByDbLm/2xwg",
            			"selfLink": "https://www.googleapis.com/books/v1/volumes/ItuaDwAAQBAJ",
            			"volumeInfo": {
            				"title": "遠離！傷人傷己的情緒風暴：3步驟！修復「關係裂痕」，覺察「內在自我」，暖心醫師的情緒管理SOP，讓你活出自己喜歡的樣子",
            				"authors": [
            					"水島廣子"
            				],
            				"publisher": "方言文化",
            				"publishedDate": "2019-06-05",
            				"description": "傷人的話憋不住，真心話卻來不及說 別讓一時失控的情緒，造成一輩子的遺憾！ 日本精神科名醫的7堂情商課， 焦慮•不爽•寂寞•罪惡感•憤怒•悲傷•悔恨 7大負面情緒＋17種心理困境，一次掃除！ 總被情緒牽著走，走不出肆虐心底的風暴？ ◆和家人陷入冷戰，卻又拉不下臉破冰，後悔不已！ ◆上司爭功諉過，身為下屬卻被迫擋槍背鍋，超不爽！ ◆明天就要上台報告，擔心自己講話結巴，心底極度焦慮！ 生活中，難免碰上讓人不安、憤怒與悲傷的事。但如果總是為此耗盡我們的能量，好運、財富與健康，恐都將離我們而去。想要打破這種困境？本書就是為你而寫的「情緒管理指南」！ ★每種情緒背後，都藏了一句悄悄話！ 情緒，是人們與生俱來的天賦，但在現代社會的壓抑氛圍下，我們往往選擇用錯誤的「常識」，來解讀情緒給我們的訊息，像是「朋友或夫妻間，越吵感情越好」、「就算氣到快抓狂，也要努力壓抑」等等。 如果你相信這些錯誤的「常識」，你的生活品質反而會一落千丈，總有一天會悶出病來，或者氣到內傷！身而為人，被賦予了情緒，我們何不善用其原始功能，創造更豐富的人生？你不知道的是，每種負面情緒的背後，都有一則不容我們忽視的警訊── 憤怒：現實與期望出現分歧，令人困擾 焦慮：缺乏足夠的資訊或行動，無法確保自身安全 不爽：受到壓抑、沒有展現「原本的自己」 悲傷：我們失去了某些重要的東西 悔恨：失去了「或許」的可能性／尊嚴受到傷害 寂寞：感受不到「心理層面的連結」 罪惡感：想要體貼他人，卻陷入「自我中心」 在知道情緒想對你說的悄悄話之後，只要再通曉正確應對的基本原則，你就掌握了「情緒管理」的SOP！如此一來，你便能靈活面對各種狀況，突破情緒的迷霧，通往解決問題的道路。 ★尋求肯定與接納，別把焦慮悶在大腦裡！ 焦慮感的來源主要有二：「資訊不足」與「自信不足」。身而為人，我們無法全知全能，會產生焦慮感，是再自然不過的事。而焦慮這種情緒其實也就是一道保險，提醒你「再多了解一下狀況吧！」此時與其把焦慮關在自己的大腦裡，不如找個人敞開心扉，訴說一下你的心情。你的親朋好友一定會給你鼓勵、為你加油；如果他們對你所焦慮的事有所了解，自然也會把資訊分享給你。如此一來，造成焦慮的兩大原因，馬上迎刃而解！ 同樣的，當別人因自身的焦慮而找上我們傾訴時，我們也應用誠懇的態度傾聽對方的焦慮，不用急著給予意見或指導，那反而會造成更大的壓力。焦慮的人要的其實很簡單，只是一個肯定的眼神、一種樂於接納的相處態度罷了。 ★情緒轉譯，怒火化為解決問題的動力！ 憤怒之所以被人們稱作「無名火」（或無明火），就是因為我們常常搞不清楚這種情緒的真實起因，於是對父母、對伴侶、對孩子口出惡言，大發雷霆，賠上了親密感情卻又解決不了問題。夫妻失和、親子關係惡劣，很多時候都是因為沒有處理好憤怒的情緒造成的。 其實，憤怒這種情緒只是很單純的想要告訴你一件事：「現實和你期望的不一樣，這種情況真是令人困擾。」你期望你的付出，別人會看見，沒想到對方沒有半句道謝；你期望孩子成績名列前茅，沒想到卻只在中段班；你期望累了一天回到家，家裡的人會慰問你，沒想到他們又叫你幫忙做東做西……面對這種情況，該怎麼解決呢？ 這時不妨用腦內的「自動翻譯機」，把「憤怒」翻譯成「困擾」──「我生氣，是因為覺得困擾，我該怎麼解決問題？或者找人幫忙呢？」如此一來，傷人的話語也就不易脫口而出。而當別人發脾氣時，你也可以將對方的情緒解讀為「陷入困擾之中」，你就不會隨之起舞，反而能用更寬容的心情去看待對方了。 ★掃除7大負面情緒＋17種心理困境，找回快樂的自己 本書透過精神醫學的角度，為你解析心理與心情的機制。書末更特別收錄17種心理困境Q & A，給你最溫暖又有建設性的建議。 Q：內心煩悶不堪，卻不知道是什麼情緒。怎麼辦？ A：把讓你煩悶的事情寫下來，然後站在自己親友的角度，想想看他們會怎麼看待這件事吧！透過書寫，更有助你了解當下的情緒。 Q：總是為了小事煩惱不已，怎麼樣才能保持平常心？ A：站在「給予」的立場，試試看以他人為中心思考事情吧～ Q：別人的話語好像總有弦外之音，讓人活得好累，該怎麼做才好？ A：說話時直話直說，聽話時單純接受言語的字面含意，和對方一起建立起真誠溝通的習慣吧！ 相信看過本書之後，你一定能夠── ．享受人際關係，彼此溝通更順暢！ ．碰上情緒化的人也不受傷害！ ．明白自己糾結的問題點！ ．不再整天鬱鬱寡歡！ 本書特色 ★日本精神科名醫專業引導，搭配療癒插畫，帶你走出讓人喘不過氣的情緒風暴！ ★7大負面情緒，正確解讀！看懂情緒背後的悄悄話，從容應對所有人際困擾！ ★17種心理困境Q & A，貼心的煩惱相談室，給你最溫暖又有建設性的建議！ 誠摯推薦 王雅涵／諮商心理師 吳若權／作家、廣播主持、企管顧問 林靜如／律師娘 瑪那熊／諮商心理師、溝通講師 蘇益賢／臨床心理師 （依姓氏筆劃排序）",
            				"readingModes": {
            					"text": true,
            					"image": true
            				},
            				"maturityRating": "NOT_MATURE",
            				"allowAnonLogging": true,
            				"contentVersion": "preview-1.0.0",
            				"panelizationSummary": {
            					"containsEpubBubbles": false,
            					"containsImageBubbles": false
            				},
            				"imageLinks": {
            					"smallThumbnail": "http://books.google.com/books/content?id=ItuaDwAAQBAJ&printsec=frontcover&img=1&zoom=5&edge=curl&source=gbs_api",
            					"thumbnail": "http://books.google.com/books/content?id=ItuaDwAAQBAJ&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api"
            				},
            				"previewLink": "http://books.google.com.tw/books?id=ItuaDwAAQBAJ&printsec=frontcover&dq=%E6%83%85%E7%B7%92&hl=&cd=3&source=gbs_api",
            				"infoLink": "https://play.google.com/store/books/details?id=ItuaDwAAQBAJ&source=gbs_api",
            				"canonicalVolumeLink": "https://play.google.com/store/books/details?id=ItuaDwAAQBAJ"
            			},
            			"saleInfo": {
            				"country": "TW",
            				"listPrice": {
            					"amount": 213,
            					"currencyCode": "TWD"
            				},
            				"retailPrice": {
            					"amount": 160,
            					"currencyCode": "TWD"
            				},
            				"buyLink": "https://play.google.com/store/books/details?id=ItuaDwAAQBAJ&rdid=book-ItuaDwAAQBAJ&rdot=1&source=gbs_api",
            				"offers": [
            					{
            						"finskyOfferType": 1,
            						"listPrice": {
            							"amountInMicros": 213000000,
            							"currencyCode": "TWD"
            						},
            						"retailPrice": {
            							"amountInMicros": 160000000,
            							"currencyCode": "TWD"
            						}
            					}
            				]
            			},
            			"accessInfo": {
            				"country": "TW",
            				"epub": {
            					"isAvailable": true,
            					"acsTokenLink": "http://books.google.com.tw/books/download/%E9%81%A0%E9%9B%A2_%E5%82%B7%E4%BA%BA%E5%82%B7%E5%B7%B1%E7%9A%84%E6%83%85%E7%B7%92%E9%A2%A8%E6%9A%B4_3%E6%AD%A5-sample-epub.acsm?id=ItuaDwAAQBAJ&format=epub&output=acs4_fulfillment_token&dl_type=sample&source=gbs_api"
            				},
            				"pdf": {
            					"isAvailable": true,
            					"acsTokenLink": "http://books.google.com.tw/books/download/%E9%81%A0%E9%9B%A2_%E5%82%B7%E4%BA%BA%E5%82%B7%E5%B7%B1%E7%9A%84%E6%83%85%E7%B7%92%E9%A2%A8%E6%9A%B4_3%E6%AD%A5-sample-pdf.acsm?id=ItuaDwAAQBAJ&format=pdf&output=acs4_fulfillment_token&dl_type=sample&source=gbs_api"
            				},
            				"accessViewStatus": "SAMPLE"
            			},
            			"searchInfo": {
            				"textSnippet": "傷人的話憋不住，真心話卻來不及說 別讓一時失控的情緒，造成一輩子的遺憾！ 日本精神科名醫的7堂情商課， 焦慮•不爽•寂寞•罪惡感•憤怒•悲傷•悔恨 ..."
            			}
            		},
            		{
            			"kind": "books#volume",
            			"id": "LRIc0AEACAAJ",
            			"etag": "aBzX1wuDquE",
            			"selfLink": "https://www.googleapis.com/books/v1/volumes/LRIc0AEACAAJ",
            			"volumeInfo": {
            				"title": "情緒, 無法翻譯",
            				"subtitle": "從文化心理學出發, 探索情緒如何被創造, 以及在不同文化之間的差異",
            				"publishedDate": "2023",
            				"readingModes": {
            					"text": false,
            					"image": false
            				},
            				"maturityRating": "NOT_MATURE",
            				"allowAnonLogging": false,
            				"contentVersion": "preview-1.0.0",
            				"panelizationSummary": {
            					"containsEpubBubbles": false,
            					"containsImageBubbles": false
            				},
            				"previewLink": "http://books.google.com.tw/books?id=LRIc0AEACAAJ&dq=%E6%83%85%E7%B7%92&hl=&cd=4&source=gbs_api",
            				"infoLink": "http://books.google.com.tw/books?id=LRIc0AEACAAJ&dq=%E6%83%85%E7%B7%92&hl=&source=gbs_api",
            				"canonicalVolumeLink": "https://books.google.com/books/about/%E6%83%85%E7%B7%92_%E7%84%A1%E6%B3%95%E7%BF%BB%E8%AD%AF.html?hl=&id=LRIc0AEACAAJ"
            			},
            			"saleInfo": {
            				"country": "TW"
            			},
            			"accessInfo": {
            				"country": "TW",
            				"epub": {
            					"isAvailable": false
            				},
            				"pdf": {
            					"isAvailable": false
            				},
            				"accessViewStatus": "NONE"
            			}
            		},
            		{
            			"kind": "books#volume",
            			"id": "5ftUEAAAQBAJ",
            			"etag": "kYkUo+A6Psk",
            			"selfLink": "https://www.googleapis.com/books/v1/volumes/5ftUEAAAQBAJ",
            			"volumeInfo": {
            				"title": "情緒芳療",
            				"subtitle": "花草力量伴你跨越情感勒索的疲憊痛楚，正視早該斷捨離的情緒振盪！",
            				"authors": [
            					"鄭雅文Vivian"
            				],
            				"publisher": "讀書共和國╱幸福文化",
            				"publishedDate": "2020-06-10",
            				"description": "情緒的冰冷暴雨之下，我，為你撐起一把傘！ 在家庭、感情、職場關係裡的無奈、無助和無能為力， 讓芳療師用香氛力量幫助你，療癒麻痺已久的心靈破口， 拒絕再讓任何「為難自己的關係」一直勒索身心！ 你是否…面對家庭、感情、職場，總覺得有說不出的委屈、解不開的結、離不開的人、流不完的淚呢？深刻為難著你的關係成了負面情緒，長期積累之下彷彿厚厚烏雲閃電，再以冰冷暴雨的無情姿態狠狠刮傷你的心，那些看似無法解套的迷途，讓芳療師以花草力量為你深層訴說、癒合傷痛吧，透過這本書，一定找到最適合自己的情緒解方。 【你是否也被「為難自己的人際關係&情緒」所苦？】 ＊為家庭付出多年的主婦，決心拿回人生下半場自主權時，卻引來全家人的不諒解… ＊從小愛搞團體、常換男友的A，總是透過人際關係索求無法被滿足的愛… ＊人生勝利組的E有長期失眠困擾，透過芳療才發現家庭陰影讓她無法釋放夢魘… ＊和男友交往多年、預計結婚的V，在幸福終點等待她的是晴天霹靂的劈腿分手… ＊習慣「躲開幸福」的女兒，原來是因為不敢比獨自拉拔她長大的媽媽幸福… ＊認真工作但被職場主管欺負的B，不反抗霸凌竟是因為被責罵會感到心安… ＊總是提拔好友的C，反遭好友無止盡索求不合理的援助，兩人因此反目… 所有的情緒，大多源自於「失衡」的關係，芳療師先以「九宮格平行思考法」為你釐清情緒關聯與對應；再以「心智圖」開拓思緒、將錯綜混亂的情緒化繁為簡；最後運用「植物香氣」給予療癒、支持與跨越的力量，漸進式引導你從自己的情緒透析看見關係的扭曲，進而從關係的扭曲發掘自己的缺乏，唯有找到情緒痛點的所在之處與不滿足，累積已久的身心窒礙才有解。 【心裡說不出的苦，讓植物為你傾訴療癒！】 ～48種植物芳療力量x25個撫心配方x11個隨身用/空間用/按摩用/沐浴用手作～ 本書收錄有益於情緒排解、釋放、重生的48種植物，芳療師就每個實際案例，給予最適合的25個撫心配方，並可搭配「第六章 撫慰情緒的芳療解方」自製成11種簡單的芳療小手作，透過吸嗅法、噴霧法、塗抹法、濕敷法、浸泡法，讓「植物香氛的溫柔強韌」、「芳療導師的舒心小語」雙重療癒你心。 *當照顧別人成了無底黑洞時… 芳療建議！【珍重自己】 香氣配方：玫瑰、檀香、甜橙 *當你因為壓力而戒斷不了「吃」的誘惑時… 芳療建議！【愉悅緩壓】 香氣配方：葡萄柚、玫瑰天竺葵、甜馬鬱蘭 *當你覺得長期夜不成眠時… 芳療建議！【擁抱幸福】 香氣配方：月桂、快樂鼠尾草、肉豆蔻 *當你感覺人際關係陷入僵局時… 芳療建議！【轉彎】 香氣配方：佛手柑、真正薰衣草、羅馬洋甘菊 *當你覺得和家人無法溝通時… 芳療建議！【信任】 香氣配方：沉香、檀香、乳香、沒藥、桂花 *當你遇到情緒化的同事或主管… 芳療建議！【排解衝突、穩定心緒】 香氣配方：檀香、黑雲杉、土木香、檸檬馬鞭草、茶樹、沉香醇百里香 *當你覺得愛情成了單向付出時… 芳療建議！【摯愛】 香氣配方：大馬士革玫瑰、永久花、快樂鼠尾草 *如果你在愛情中找不到自己… 芳療建議！【回歸真我】 香氣配方：快樂鼠尾草、沈香醇百里香、玫瑰草 對於卡在任何一段關係裡的迷惘，其實能借助不同植物屬性的力量，透析、釐清、探索自己當下的癥結點，本書收錄Vivian老師對於每段關係最適切的建議、予每個情緒最妥貼的安慰，加以舒心理清，注入跨越突破的勇氣；想更加了解植物療癒本質、香氣特徵、調油搭檔、療癒性質、使用規範、情緒洞悉的讀者，還可翻看「附錄：適用於情緒芳療之植物介紹」，藉此得到更完整的植物芳療資訊。 暖心推薦 女巫阿娥／芳香療法與香藥草生活保健作家 王羽暄／《骨盆回正》作者、台灣行動瑜伽協會創會理事長 Claudia／Claudia Studio 女巫的塔羅．芳療",
            				"readingModes": {
            					"text": true,
            					"image": true
            				},
            				"maturityRating": "NOT_MATURE",
            				"allowAnonLogging": true,
            				"contentVersion": "preview-1.0.0",
            				"panelizationSummary": {
            					"containsEpubBubbles": false,
            					"containsImageBubbles": false
            				},
            				"imageLinks": {
            					"smallThumbnail": "http://books.google.com/books/content?id=5ftUEAAAQBAJ&printsec=frontcover&img=1&zoom=5&edge=curl&source=gbs_api",
            					"thumbnail": "http://books.google.com/books/content?id=5ftUEAAAQBAJ&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api"
            				},
            				"previewLink": "http://books.google.com.tw/books?id=5ftUEAAAQBAJ&printsec=frontcover&dq=%E6%83%85%E7%B7%92&hl=&cd=5&source=gbs_api",
            				"infoLink": "https://play.google.com/store/books/details?id=5ftUEAAAQBAJ&source=gbs_api",
            				"canonicalVolumeLink": "https://play.google.com/store/books/details?id=5ftUEAAAQBAJ"
            			},
            			"saleInfo": {
            				"country": "TW",
            				"listPrice": {
            					"amount": 309,
            					"currencyCode": "TWD"
            				},
            				"retailPrice": {
            					"amount": 210,
            					"currencyCode": "TWD"
            				},
            				"buyLink": "https://play.google.com/store/books/details?id=5ftUEAAAQBAJ&rdid=book-5ftUEAAAQBAJ&rdot=1&source=gbs_api",
            				"offers": [
            					{
            						"finskyOfferType": 1,
            						"listPrice": {
            							"amountInMicros": 309000000,
            							"currencyCode": "TWD"
            						},
            						"retailPrice": {
            							"amountInMicros": 210000000,
            							"currencyCode": "TWD"
            						}
            					}
            				]
            			},
            			"accessInfo": {
            				"country": "TW",
            				"epub": {
            					"isAvailable": true,
            					"acsTokenLink": "http://books.google.com.tw/books/download/%E6%83%85%E7%B7%92%E8%8A%B3%E7%99%82-sample-epub.acsm?id=5ftUEAAAQBAJ&format=epub&output=acs4_fulfillment_token&dl_type=sample&source=gbs_api"
            				},
            				"pdf": {
            					"isAvailable": true,
            					"acsTokenLink": "http://books.google.com.tw/books/download/%E6%83%85%E7%B7%92%E8%8A%B3%E7%99%82-sample-pdf.acsm?id=5ftUEAAAQBAJ&format=pdf&output=acs4_fulfillment_token&dl_type=sample&source=gbs_api"
            				},
            				"accessViewStatus": "SAMPLE"
            			},
            			"searchInfo": {
            				"textSnippet": "情緒的冰冷暴雨之下，我，為你撐起一把傘！ 在家庭、感情、職場關係裡的無奈、無助和無能為力， 讓芳療師用香氛力量幫助你，療癒麻痺已久的心靈破口， ..."
            			}
            		},
            		{
            			"kind": "books#volume",
            			"id": "qnPhDwAAQBAJ",
            			"etag": "js1OFPvaDLc",
            			"selfLink": "https://www.googleapis.com/books/v1/volumes/qnPhDwAAQBAJ",
            			"volumeInfo": {
            				"title": "情緒表達的15個高EQ管理術",
            				"subtitle": "調整壞情緒，調適好壓力，教孩子人生必備力量，樂觀、同理、傾訴、勇敢面對挫折，培育孩子堅強的心靈，提升生命的韌性",
            				"authors": [
            					"喬冰"
            				],
            				"publisher": "漢湘文化（漢湘）",
            				"publishedDate": "2020-01-15",
            				"description": "給國中小學生的 ＼ 幸福情緒表達課 ／ 解決15個孩子心靈成長困境 做情緒的小主人，學會調適負面情緒， 培育堅強心靈的人生力量 ☆ 誰適合這本書？ ■ 給常常被悲觀性格困住，想樂觀思考、打擊壞情緒的孩子 ■ 想要和孩子提升正向情緒交流的家長 ■ 給想學習換位思考、培養高EQ的孩子 ■ 尚未能向他人好好表達自己情緒的孩子 ☆ 閱讀本書能帶來什麼幫助？ ■ 成為情緒的主人，在生活中運用正向情緒傳達自己的心意 ■ 擁有調適壓力的能力，學會傾訴，身心更健康 ■ 寬容化解衝突，讓心平靜，不受憤怒所限制 ■ 學習與孤獨共處，培養興趣，適應獨處時光，內心就會更強大 小葛和柔柔都被頑皮的弟弟亂畫了她們的裙子， 小葛氣得大吼大叫，甚至摔弟弟的畫筆出氣， 這樣做卻沒有讓她消氣，反而氣到肚子都痛了…… 肖肖的小倉鼠過世了，她不吃不喝哭了一個早上； 法揚輸掉了機器人比賽，整天關在黑漆漆的房間裡，都不出門…… 遇到傷心事時，我們該怎麼面對？該怎麼宣洩低潮呢？ 子默代表全班參加繪畫大賽，因為怕自己表現不好， 龐大的壓力使他情緒波動大、難以入眠…… 當壓力「爆表」的時候該怎麼做呢？ 書南只要獨處的時候就上網找網友聊天， 忍受不了孤獨，總覺得獨處很可怕，直到他找到自己的興趣…… 當孩子不能獨處時，該如何協助他，以及如何讓孩子發展興趣？ 以上是不是都是孩子平時情緒上會遇到的情況？ 要如何真正的提升情緒管理，樂觀思考，有勇氣面對人生中必會襲來的負面情緒？ 跟著漫畫中的主角一同修練「情緒高手EQ提升術」吧！ ☆ 衝動的時候，不要做決定 生氣的時候，人們往往變得很衝動，此時說的話、做的事都欠缺思考，很容易做出愚蠢、危險的舉動。 → 高EQ提升術 1 控制憤怒行為，才能有效解決問題 生氣的時候大發脾氣，不僅不能讓事情好轉，還可能傷害了別人，請將「生氣」以冷靜理智的方式表達出來，並且說出自己生氣的原因，問題才能得到解決。 ☆ 學習面對傷心的力量，讓心更堅強 悲傷是人生必須經歷的過程，每個人都會遇到令自己傷心的事情，就像被朋友誤解時和失去心愛的東西時，都會傷心流淚。 → 高EQ提升術 2 運動是治療悲傷最好的方法 運動是宣洩低潮情緒的良好方式，運動能讓人身心放鬆，從悲傷事件轉移，不再沉浸於負面情緒中。最重要的是接受傷心的事實，學會傾訴，就能產生戰勝悲傷的力量。 ☆ 適度的壓力使你更有創造力 壓力會帶給我們應對挑戰的力量，但當壓力過大時，卻會讓我們吃不下、睡不著，甚至被壓力所壓垮。 → 高EQ提升術 3 與壓力友好相處最重要 學習與壓力友好相處，控管壓力，學會減壓，壓力會讓你變得更厲害。 ☆ 情緒是孩子成長中的「小關卡」 孩子的成長階段中，一定都會有名為「情緒」的小關卡，孩子在情緒表達中，傳達自己的想法與心意；在面對不同的事件發生中，內心也會產生不同的情緒：有時開心、有時悲傷、有時憤怒、有時低潮。如何讓孩子正確解讀這些情緒，並運用實用方法去管理情緒，是學齡中孩子人生中最重要的課題。 本書提供生活中一定會發生的情緒漫畫，帶領孩子理解自己，並協助爸媽們培養孩子的EQ控制力，不僅能讓孩子有效處理在日常、人際及事件面對到的各種危機，更能讓孩子在人生的道路上持續的幸福、堅韌，勇敢的走下去。 本書特色 【特色 1】幽默豐富的情境漫畫，介紹樂觀幸福的情緒管理策略，讓孩子一看就懂 【特色 2】專為青少年族群打造，有效解決負面情緒困擾，學習讓心平靜下來 【特色 3】協助解決孩子的15種心靈成長困境，給孩子最暖心、建設性的建議",
            				"readingModes": {
            					"text": false,
            					"image": true
            				},
            				"maturityRating": "NOT_MATURE",
            				"allowAnonLogging": false,
            				"contentVersion": "0.1.0.0.preview.1",
            				"panelizationSummary": {
            					"containsEpubBubbles": false,
            					"containsImageBubbles": false
            				},
            				"imageLinks": {
            					"smallThumbnail": "http://books.google.com/books/content?id=qnPhDwAAQBAJ&printsec=frontcover&img=1&zoom=5&edge=curl&source=gbs_api",
            					"thumbnail": "http://books.google.com/books/content?id=qnPhDwAAQBAJ&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api"
            				},
            				"previewLink": "http://books.google.com.tw/books?id=qnPhDwAAQBAJ&printsec=frontcover&dq=%E6%83%85%E7%B7%92&hl=&cd=6&source=gbs_api",
            				"infoLink": "https://play.google.com/store/books/details?id=qnPhDwAAQBAJ&source=gbs_api",
            				"canonicalVolumeLink": "https://play.google.com/store/books/details?id=qnPhDwAAQBAJ"
            			},
            			"saleInfo": {
            				"country": "TW",
            				"listPrice": {
            					"amount": 162,
            					"currencyCode": "TWD"
            				},
            				"retailPrice": {
            					"amount": 122,
            					"currencyCode": "TWD"
            				},
            				"buyLink": "https://play.google.com/store/books/details?id=qnPhDwAAQBAJ&rdid=book-qnPhDwAAQBAJ&rdot=1&source=gbs_api",
            				"offers": [
            					{
            						"finskyOfferType": 1,
            						"listPrice": {
            							"amountInMicros": 162000000,
            							"currencyCode": "TWD"
            						},
            						"retailPrice": {
            							"amountInMicros": 122000000,
            							"currencyCode": "TWD"
            						}
            					}
            				]
            			},
            			"accessInfo": {
            				"country": "TW",
            				"epub": {
            					"isAvailable": false
            				},
            				"pdf": {
            					"isAvailable": true,
            					"acsTokenLink": "http://books.google.com.tw/books/download/%E6%83%85%E7%B7%92%E8%A1%A8%E9%81%94%E7%9A%8415%E5%80%8B%E9%AB%98EQ%E7%AE%A1%E7%90%86%E8%A1%93-sample-pdf.acsm?id=qnPhDwAAQBAJ&format=pdf&output=acs4_fulfillment_token&dl_type=sample&source=gbs_api"
            				},
            				"accessViewStatus": "SAMPLE"
            			},
            			"searchInfo": {
            				"textSnippet": "給國中小學生的 ＼ 幸福情緒表達課 ／ 解決15個孩子心靈成長困境 做情緒的小主人，學會調適負面情緒， 培育堅強心靈的人生力量 ☆ 誰適合這本書？ ■ ..."
            			}
            		},
            		{
            			"kind": "books#volume",
            			"id": "vGOhDwAAQBAJ",
            			"etag": "LMvYDzZeDsg",
            			"selfLink": "https://www.googleapis.com/books/v1/volumes/vGOhDwAAQBAJ",
            			"volumeInfo": {
            				"title": "隔絕情緒勒索，給自己好溫暖的心情整理術",
            				"subtitle": "暖心醫師教你，學會想法轉個彎，90%人際煩惱立即消除，重新找回自在人生",
            				"authors": [
            					"水島廣子"
            				],
            				"publisher": "健康你好（方言）",
            				"publishedDate": "2019-07-10",
            				"description": "婆媳難相處、主管惡質、同事使壞， 讓你痛苦又無法脫身？！ 8大煩惱X 31則溫暖心情整理術， 暖心名醫教你學會隔絕「情緒勒索」，劃出自我防護的人際界線。 ►下班前被主管臨時交付任務，要求隔天完成，超不爽！ ►無法和同事自在地相處，心裡好焦躁。 ►擔心孩子在學校被孤立，不得不和同學們的媽媽交流，壓力好大！ ►婆婆常干涉「生活習慣」、「孩子管教方式」，讓人既氣結又不知如何是好！ 日常生活中，每個人都可能身兼不同角色、周旋在數種人際關係中，難免會碰到人、事、物的困擾，進而交織出如上述的複雜情緒問題。 ★煩躁、不爽、焦慮……，讓人際關係崩壞的8大情緒煩惱 所有的「煩惱」，都來自於「人際關係」。面對這些看似稀鬆平常、三不五時就會出現「煩惱」，一旦放任不管，就可能會像星星之火般，輕易地占據思緒，讓心中充滿負面情緒；甚至影響生活節奏，危害身心健康，並惡化人際關係。例如── ►焦慮：對事物猶豫不決，喪失自信 ►壓力：目標設定過高，無法發揮實力 ►煩躁：計畫走向不如預期，有慢性化憤怒傾向 ►不爽：遭受對方不合理的對待，心裡疲憊不堪 ►軟弱：掩飾無法滿足的心，逃避現實以自保 ►無力感：對於過去，產生悔意與失落感 ►心痛：原先穩定的關係，開始產生崩解 ►憂鬱：往常的失敗經驗，引發負面情緒連鎖效應 ★負面思緒轉個彎，擺脫令人窒息的「焦慮感」 焦慮，主要原因來自於「過分堅持」和「完美主義」。許多容易焦慮的人，總是會深陷在「應該這麼做才對」、「那樣做還不夠好」、「這不在我的計畫中」、「決策錯誤就代表失敗」、「不想後悔」等思緒中。 適度的焦慮，或許可以成為你生活和工作上的動力；然而，一旦過度焦慮，將可能讓你出現偏執、優柔寡斷、專注力喪失、失眠等行為；長期下來，將被這股焦慮感所綁架，甚至進入憂鬱的惡性循環。 事實上，這世上並沒有完美無瑕、毫無失敗經驗的人生。無論對與錯，人生中所發生的大小事情，都蘊藏著值得回味、享受的樂趣。因此，如果「焦慮感」再度浮現，不妨換個角度看事情，對已經發生的過程「放手」，將有助於消除你心中的煩悶，避免在負面情緒中打轉。 ★面對「情緒勒索」，暖心醫師幫你找解方 長期研究人際關係療法的日本精神科名醫水島廣子認為：「人，雖然因為人際關係而煩惱，但也可以在依靠他人的支持下，重新振作起來。」也就是說，解決煩惱問題的關鍵，其實也存在於人與人的互動過程中。 藉由精神醫學的角度，她解析無數因煩惱而起的情緒勒索案例，進而歸納出31則心情整理術。希望能幫助有上述煩惱的你，從中尋找到「勇氣」和「解決辦法」，讓自己的心變得更溫暖，漸漸遠離被負面情緒綁架的陰影。舉例來說── 【情境一】主管告訴你：「若這個案子沒有獲利，下個月減薪！」 【煩惱根源】急迫的語氣、過高的期望，這些「無聲的壓力」，讓你無法喘息、心神不寧。 【解方】先假裝順從，讓主管安心。接著，坦率詢問：「我只要做○○就好嗎？」確立責任範圍。 【情境二】同事似笑非笑表示：「想不到你這個月成績不錯嘛，老闆對你讚譽有加呢！」 【煩惱根源】表面上像是讚美，但微妙的表情和口吻，讓你覺得「話中有話」，進而陷入「焦慮」情緒中。 【解方】不需要擅自解讀對方的話語。既然表面聽起來是讚美，就坦然地向對方道謝即可。 本書特色 ‧針對平日常見「8大情緒煩惱」，從案例情境切入，提供具體的「31則溫暖心情整理術」。 ‧專業醫師版的「心靈小語」，貼近心理需求，讓每個人都能從中找到專屬的一帖「良藥」。 國內推薦 （依姓氏筆畫排序） 諮商心理師 王雅涵 律師娘 林靜如 諮商心理師／作家 黃之盈 臨床心理師 蘇益賢",
            				"readingModes": {
            					"text": true,
            					"image": true
            				},
            				"maturityRating": "NOT_MATURE",
            				"allowAnonLogging": true,
            				"contentVersion": "1.2.2.0.preview.3",
            				"panelizationSummary": {
            					"containsEpubBubbles": false,
            					"containsImageBubbles": false
            				},
            				"imageLinks": {
            					"smallThumbnail": "http://books.google.com/books/content?id=vGOhDwAAQBAJ&printsec=frontcover&img=1&zoom=5&edge=curl&source=gbs_api",
            					"thumbnail": "http://books.google.com/books/content?id=vGOhDwAAQBAJ&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api"
            				},
            				"previewLink": "http://books.google.com.tw/books?id=vGOhDwAAQBAJ&printsec=frontcover&dq=%E6%83%85%E7%B7%92&hl=&cd=7&source=gbs_api",
            				"infoLink": "https://play.google.com/store/books/details?id=vGOhDwAAQBAJ&source=gbs_api",
            				"canonicalVolumeLink": "https://play.google.com/store/books/details?id=vGOhDwAAQBAJ"
            			},
            			"saleInfo": {
            				"country": "TW",
            				"listPrice": {
            					"amount": 213,
            					"currencyCode": "TWD"
            				},
            				"retailPrice": {
            					"amount": 160,
            					"currencyCode": "TWD"
            				},
            				"buyLink": "https://play.google.com/store/books/details?id=vGOhDwAAQBAJ&rdid=book-vGOhDwAAQBAJ&rdot=1&source=gbs_api",
            				"offers": [
            					{
            						"finskyOfferType": 1,
            						"listPrice": {
            							"amountInMicros": 213000000,
            							"currencyCode": "TWD"
            						},
            						"retailPrice": {
            							"amountInMicros": 160000000,
            							"currencyCode": "TWD"
            						}
            					}
            				]
            			},
            			"accessInfo": {
            				"country": "TW",
            				"epub": {
            					"isAvailable": true,
            					"acsTokenLink": "http://books.google.com.tw/books/download/%E9%9A%94%E7%B5%95%E6%83%85%E7%B7%92%E5%8B%92%E7%B4%A2_%E7%B5%A6%E8%87%AA%E5%B7%B1%E5%A5%BD%E6%BA%AB%E6%9A%96%E7%9A%84-sample-epub.acsm?id=vGOhDwAAQBAJ&format=epub&output=acs4_fulfillment_token&dl_type=sample&source=gbs_api"
            				},
            				"pdf": {
            					"isAvailable": true,
            					"acsTokenLink": "http://books.google.com.tw/books/download/%E9%9A%94%E7%B5%95%E6%83%85%E7%B7%92%E5%8B%92%E7%B4%A2_%E7%B5%A6%E8%87%AA%E5%B7%B1%E5%A5%BD%E6%BA%AB%E6%9A%96%E7%9A%84-sample-pdf.acsm?id=vGOhDwAAQBAJ&format=pdf&output=acs4_fulfillment_token&dl_type=sample&source=gbs_api"
            				},
            				"accessViewStatus": "SAMPLE"
            			},
            			"searchInfo": {
            				"textSnippet": "婆媳難相處、主管惡質、同事使壞， 讓你痛苦又無法脫身？！ 8大煩惱X 31則溫暖心情整理術， 暖心名醫教你學會隔絕「情緒勒索」，劃出自我防護的人際界線。 ..."
            			}
            		},
            		{
            			"kind": "books#volume",
            			"id": "MxozBESi9OYC",
            			"etag": "dYjlqyeaQyI",
            			"selfLink": "https://www.googleapis.com/books/v1/volumes/MxozBESi9OYC",
            			"volumeInfo": {
            				"title": "情緒涵養",
            				"authors": [
            					"饒見維"
            				],
            				"publisher": "台灣五南圖書出版股份有限公司",
            				"publishedDate": "2015-07-12",
            				"description": "人類天生就會「趨樂避苦」，本書主要關切的就是：如何成為快樂的人？基於此，本書除了探討情緒涵養的重要性之外，更介紹一套完整的情緒涵養方法，從「治標法」到「治本法」到「究竟法」，逐步引導讀者學習如何掌握自己的情緒。作者所要傳遞的一個重要訊息就是：每一個人的內在都具有無限的快樂泉源。本書會分享一套和這個快樂泉源產生聯結的方法，一個人一旦學會了這一套方法，就可以在生命的大海中悠遊自在，過著快樂的生活。",
            				"readingModes": {
            					"text": false,
            					"image": true
            				},
            				"maturityRating": "NOT_MATURE",
            				"allowAnonLogging": true,
            				"contentVersion": "0.9.11.0.preview.1",
            				"panelizationSummary": {
            					"containsEpubBubbles": false,
            					"containsImageBubbles": false
            				},
            				"imageLinks": {
            					"smallThumbnail": "http://books.google.com/books/content?id=MxozBESi9OYC&printsec=frontcover&img=1&zoom=5&edge=curl&source=gbs_api",
            					"thumbnail": "http://books.google.com/books/content?id=MxozBESi9OYC&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api"
            				},
            				"previewLink": "http://books.google.com.tw/books?id=MxozBESi9OYC&pg=PA13&dq=%E6%83%85%E7%B7%92&hl=&cd=8&source=gbs_api",
            				"infoLink": "https://play.google.com/store/books/details?id=MxozBESi9OYC&source=gbs_api",
            				"canonicalVolumeLink": "https://play.google.com/store/books/details?id=MxozBESi9OYC"
            			},
            			"saleInfo": {
            				"country": "TW",
            				"listPrice": {
            					"amount": 368,
            					"currencyCode": "TWD"
            				},
            				"retailPrice": {
            					"amount": 250,
            					"currencyCode": "TWD"
            				},
            				"buyLink": "https://play.google.com/store/books/details?id=MxozBESi9OYC&rdid=book-MxozBESi9OYC&rdot=1&source=gbs_api",
            				"offers": [
            					{
            						"finskyOfferType": 1,
            						"listPrice": {
            							"amountInMicros": 368000000,
            							"currencyCode": "TWD"
            						},
            						"retailPrice": {
            							"amountInMicros": 250000000,
            							"currencyCode": "TWD"
            						}
            					}
            				]
            			},
            			"accessInfo": {
            				"country": "TW",
            				"epub": {
            					"isAvailable": false
            				},
            				"pdf": {
            					"isAvailable": true,
            					"acsTokenLink": "http://books.google.com.tw/books/download/%E6%83%85%E7%B7%92%E6%B6%B5%E9%A4%8A-sample-pdf.acsm?id=MxozBESi9OYC&format=pdf&output=acs4_fulfillment_token&dl_type=sample&source=gbs_api"
            				},
            				"accessViewStatus": "SAMPLE"
            			},
            			"searchInfo": {
            				"textSnippet": "... <b>情緒</b>涵養」這個詞。這是我獨創的一個詞,這個詞會根據上下文的脈絡而有不同的意義。這個詞有時是一個動詞,有時是一個名詞。當動詞用時,「涵養」意謂著「包涵」、「照顧」、「養育」、「愛護」、「觀照 ... <b>情緒</b>涵養之歷程。 02 <b>情緒</b>涵養的意義與目標 013."
            			}
            		},
            		{
            			"kind": "books#volume",
            			"id": "puAiEAAAQBAJ",
            			"etag": "qiqcgb3yitE",
            			"selfLink": "https://www.googleapis.com/books/v1/volumes/puAiEAAAQBAJ",
            			"volumeInfo": {
            				"title": "情緒，如何療癒",
            				"subtitle": "憂慮、憤怒、壓力和憂鬱的15個情緒解答",
            				"authors": [
            					"諾曼‧萊特 (H. Norman Wright)"
            				],
            				"publisher": "宇宙光全人關懷機構",
            				"publishedDate": "2021-01-25",
            				"description": "★ 紐約時報暢銷作家、心理諮商師──諾曼．萊特最新力作！ 認識生命中影響巨大的四種負向情緒， 諮商大師萊特深入解答憂慮、憤怒、壓力與憂鬱的成因， 提供勝過這四類情緒的15個有效方法，讓我們用健康的態度面對它們， 更幫助我們找出自己生命中的弱點，讓負向的情緒影響轉而成為祝福。 憂慮就像霧，它遮蔽現實，它冷冽像能鑽進骨頭裡，它遮斷了溫暖的陽光。假如我們能穿透憂慮的濃霧，看得見未來，就能看見我們真正的問題在哪裡了。 憤怒，這個有爭議性又常被誤解的情緒，它影響到我們每一個人，無論我們喜不喜歡它，它仍常在身邊。生氣是可加以管理的，就像許多其他感受一樣——感受本身並不分對錯，問題出在錯誤地處理怒氣。 許多時候並不是哪一件事導致你壓力大，那麼問題的起源在哪裡？壓力的情形大多牽涉到自己和外在世界間產生某種衝突。我們的壓力多半來自哪裡？無論兒童、青少年或成人，壓力都來自我們自己的心思意念。 憂鬱不像悲傷，悲傷是因為失望或喪失而來的低落感，沒多久，那股低氣壓會解除，你仍能行使正常功能。憂鬱就不同了：它持續更久而且更強烈。憂鬱把希望之窗關閉，有時甚至拉下黑色簾幕。 本書特色 １. 全書分為四篇：憂慮、憤怒、壓力與憂鬱，可以按照主題逐步進入情緒的探索，使閱讀層次很清楚，也容易讀。 ２. 作者關懷女性與男性的不同生活處境，在壓力篇和憂鬱篇中，特將男性與女性的壓力與憂鬱分別講述。 ３. 幾乎每一篇都有列出一些幫助管理情緒的聖經金句。 ４. 作者指出每一種情緒的正向與負向層面，讓讀者用健康的心態認識自己的情緒，既不否認自己的情緒反應，也不逃避負向情緒的後果，而是積極找出適合自己的方法，能讓各種情緒找到平衡。",
            				"readingModes": {
            					"text": false,
            					"image": true
            				},
            				"maturityRating": "NOT_MATURE",
            				"allowAnonLogging": true,
            				"contentVersion": "preview-1.0.0",
            				"panelizationSummary": {
            					"containsEpubBubbles": false,
            					"containsImageBubbles": false
            				},
            				"imageLinks": {
            					"smallThumbnail": "http://books.google.com/books/content?id=puAiEAAAQBAJ&printsec=frontcover&img=1&zoom=5&edge=curl&source=gbs_api",
            					"thumbnail": "http://books.google.com/books/content?id=puAiEAAAQBAJ&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api"
            				},
            				"previewLink": "http://books.google.com.tw/books?id=puAiEAAAQBAJ&printsec=frontcover&dq=%E6%83%85%E7%B7%92&hl=&cd=9&source=gbs_api",
            				"infoLink": "https://play.google.com/store/books/details?id=puAiEAAAQBAJ&source=gbs_api",
            				"canonicalVolumeLink": "https://play.google.com/store/books/details?id=puAiEAAAQBAJ"
            			},
            			"saleInfo": {
            				"country": "TW",
            				"listPrice": {
            					"amount": 294,
            					"currencyCode": "TWD"
            				},
            				"retailPrice": {
            					"amount": 200,
            					"currencyCode": "TWD"
            				},
            				"buyLink": "https://play.google.com/store/books/details?id=puAiEAAAQBAJ&rdid=book-puAiEAAAQBAJ&rdot=1&source=gbs_api",
            				"offers": [
            					{
            						"finskyOfferType": 1,
            						"listPrice": {
            							"amountInMicros": 294000000,
            							"currencyCode": "TWD"
            						},
            						"retailPrice": {
            							"amountInMicros": 200000000,
            							"currencyCode": "TWD"
            						}
            					}
            				]
            			},
            			"accessInfo": {
            				"country": "TW",
            				"epub": {
            					"isAvailable": false
            				},
            				"pdf": {
            					"isAvailable": true,
            					"acsTokenLink": "http://books.google.com.tw/books/download/%E6%83%85%E7%B7%92_%E5%A6%82%E4%BD%95%E7%99%82%E7%99%92-sample-pdf.acsm?id=puAiEAAAQBAJ&format=pdf&output=acs4_fulfillment_token&dl_type=sample&source=gbs_api"
            				},
            				"accessViewStatus": "SAMPLE"
            			},
            			"searchInfo": {
            				"textSnippet": "★ 紐約時報暢銷作家、心理諮商師──諾曼．萊特最新力作！ 認識生命中影響巨大的四種負向情緒， 諮商大師萊特深入解答憂慮、憤怒、壓力與憂鬱的成因， ..."
            			}
            		},
            		{
            			"kind": "books#volume",
            			"id": "Mxp4EAAAQBAJ",
            			"etag": "GEhEto03J8I",
            			"selfLink": "https://www.googleapis.com/books/v1/volumes/Mxp4EAAAQBAJ",
            			"volumeInfo": {
            				"title": "心情之書",
            				"subtitle": "擺脫爛情緒泥淖，我的美好生活要訣",
            				"authors": [
            					"蘿倫•馬汀（Lauren Martin）"
            				],
            				"publisher": "讀書共和國╱行路",
            				"publishedDate": "2022-06-29",
            				"description": "亞馬遜網路書店選書，讀者評價4.6星高分 擺脫七大類情緒誤區的實戰演練！ ＊＊＊＊＊ 五年前，蘿倫•馬汀認為自己肯定哪裡有問題。她在紐約擁有一份好工作、住在布魯克林一間舒適的公寓、有個貼心負責的男友，但是她每天都在跟自卑、焦慮和暴躁易怒等負面情緒搏鬥。 某天，她偶遇一名迷人又成功的陌生女子，發現這個人生勝利組居然跟她有相同的感受。她這才意識到，人人都有自己的問題要面對，而女性又因先天與後天因素，尤其有一些獨特的情緒困擾。她們都跟她一樣：茫然、憂鬱、陰晴不定，並且渴望改變。於是蘿倫決定研究壞心情會帶來什麼影響，並且探索有什麼方法能夠扭轉它們。她藉由廣泛閱讀，從眾多知名人士的經驗談，找到人生智慧的結晶，持續將它們收進部落格裡，這些分享快速吸引了全球各地的讀者。 在這些讀者啟發下，她決定用這本書，進一步紀錄下自己為生活找回平靜的心路歷程。她花了數年刻意察覺、記錄、理解自己心情不好的時刻，例如：與某友人撕破臉，連帶斷了共同朋友；拖拖拉拉，不想面對工作的週日恐慌；想要對別人好，最終因為不耐反而崩潰發飆；久久回老家一趟，卻總是跟家人吵架……等等。在本書每一章，她探索觸發壞心情的一類刺激，辨認出它們真正的樣貌，並記述接下來自己如何花時間努力探究它、解析它，藉由它來練習，幫助自己成長。 《心情之書》中，蘿倫•馬汀將科學研究、雋永哲理與有效的自我照顧方法，融合在一起，除了整理別人因應各類情緒刺激的思考，亦娓娓說明焦慮、失望與自我意識等的運作原理，最後提出明確實用又好實行的作法，寫成了一部動人又能引發廣泛共鳴的個人轉變史。讀著這本書，你會自然而然學著拉開距離觀察自己，並可望透過書中實用的建議，擺脫心情易受外界影響的惡性循環，變得有能力克服、走過以及接受生命的低潮時刻，並且把這些時刻化為知識、力量與平靜。 ▎本書共鳴選句（最前面數字為所屬章節）： （1）練習留意自己的思緒有一個好方法，那就是練習留意其他事情。 （1）「你有什麼凍齡祕訣嗎？」「不要有壓力。……（年輕時）因為發生一件事，我突然開竅。當我面對該處理的事情時，我不會覺得非做不可，而是覺得我有機會去做這些事。」 （3）情緒成熟的意思是知道自己的極限在哪。必須說「不」的時候，果斷說「不」。 （4）情緒有感染力。我們從別人身上感染到的情緒，會使我們喜歡對方，獲釋討厭對方。對方的反應創造出我們感受到的能量。 （4）你不可能時時刻刻都開心，這是不切實際的目標。我們的目標應該是時時刻刻都要愛自己。愛自己跟開心不一樣，開心只是一瞬。 （4）愛自己就是少一點自我批判，少一點過度分析，少一點負面的自言自語。",
            				"readingModes": {
            					"text": true,
            					"image": true
            				},
            				"maturityRating": "NOT_MATURE",
            				"allowAnonLogging": true,
            				"contentVersion": "preview-1.0.0",
            				"panelizationSummary": {
            					"containsEpubBubbles": false,
            					"containsImageBubbles": false
            				},
            				"imageLinks": {
            					"smallThumbnail": "http://books.google.com/books/content?id=Mxp4EAAAQBAJ&printsec=frontcover&img=1&zoom=5&edge=curl&source=gbs_api",
            					"thumbnail": "http://books.google.com/books/content?id=Mxp4EAAAQBAJ&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api"
            				},
            				"previewLink": "http://books.google.com.tw/books?id=Mxp4EAAAQBAJ&printsec=frontcover&dq=%E6%83%85%E7%B7%92&hl=&cd=10&source=gbs_api",
            				"infoLink": "https://play.google.com/store/books/details?id=Mxp4EAAAQBAJ&source=gbs_api",
            				"canonicalVolumeLink": "https://play.google.com/store/books/details?id=Mxp4EAAAQBAJ"
            			},
            			"saleInfo": {
            				"country": "TW",
            				"listPrice": {
            					"amount": 309,
            					"currencyCode": "TWD"
            				},
            				"retailPrice": {
            					"amount": 210,
            					"currencyCode": "TWD"
            				},
            				"buyLink": "https://play.google.com/store/books/details?id=Mxp4EAAAQBAJ&rdid=book-Mxp4EAAAQBAJ&rdot=1&source=gbs_api",
            				"offers": [
            					{
            						"finskyOfferType": 1,
            						"listPrice": {
            							"amountInMicros": 309000000,
            							"currencyCode": "TWD"
            						},
            						"retailPrice": {
            							"amountInMicros": 210000000,
            							"currencyCode": "TWD"
            						}
            					}
            				]
            			},
            			"accessInfo": {
            				"country": "TW",
            				"epub": {
            					"isAvailable": true,
            					"acsTokenLink": "http://books.google.com.tw/books/download/%E5%BF%83%E6%83%85%E4%B9%8B%E6%9B%B8-sample-epub.acsm?id=Mxp4EAAAQBAJ&format=epub&output=acs4_fulfillment_token&dl_type=sample&source=gbs_api"
            				},
            				"pdf": {
            					"isAvailable": true,
            					"acsTokenLink": "http://books.google.com.tw/books/download/%E5%BF%83%E6%83%85%E4%B9%8B%E6%9B%B8-sample-pdf.acsm?id=Mxp4EAAAQBAJ&format=pdf&output=acs4_fulfillment_token&dl_type=sample&source=gbs_api"
            				},
            				"accessViewStatus": "SAMPLE"
            			},
            			"searchInfo": {
            				"textSnippet": "亞馬遜網路書店選書，讀者評價4.6星高分 擺脫七大類情緒誤區的實戰演練！ ＊＊＊＊＊ 五年前，蘿倫•馬汀認為自己肯定哪裡有問題。她在紐約擁有一份好工作、住在布魯克林一間 ..."
            			}
            		}
            	]
            }
        """.trimIndent()

class GoogleRemoteSearchBooksDataSourceTest{
    @Test
    fun search() {
        val testEngine = MockEngine { request ->
            respond(
                content = ByteReadChannel(response),
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        runBlocking {
            GoogleBooksDataSource(testEngine)
                .search("情緒")
                .shouldNotBeNull()
        }
    }
}
