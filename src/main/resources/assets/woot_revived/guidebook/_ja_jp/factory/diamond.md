---
navigation:
    parent: factory/factory-index.md
    title: "ダイヤモンド"
    icon: "woot_revived:diamond_cell"
    position: 4
---
# ダイヤモンド

ファクトリーの4つ目ティア。

## デザイン

プレビューで小さく表示される疑似スポナーはオプションであることに注意してください。

4つ目のティアに設定した<ItemImage id="layout" scale="0.5"/>[レイアウト](../machines-blocks/layout.md#iron)、は建設をするのに役に立ちます。

<GameScene zoom="2.5" interactive={true}>
    <ImportStructure src="../assets/factory/diamond.snbt" />
    <IsometricCamera yaw="195" pitch="6" />
</GameScene>

## ダイヤモンドティアのブロック

<Row>
  <BlockImage id="diamond_pylon" scale="4" p:attached="true" />
  <BlockImage id="diamond_plinth" scale="4" p:attached="true" />
  <BlockImage id="diamond_cell" scale="4" p:attached="true" />
</Row>


これには、
<ItemImage id="diamond_pylon" scale="0.5"/>ダイヤモンドのパイロンが32個、
<ItemImage id="diamond_plinth" scale="0.5"/>ダイヤモンドの土台が32個
<ItemImage id="diamond_cell" scale="0.5"/>ダイヤモンドのバイタリティセルが1個必要です。

<Row>
  <RecipeFor id="diamond_pylon" />
  <RecipeFor id="diamond_plinth" />
  <RecipeFor id="diamond_cell" />
</Row>