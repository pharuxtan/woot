---
navigation:
    parent: factory/factory-index.md
    title: "ネザライト"
    icon: "woot_revived:netherite_cell"
    position: 5
---
# ネザライト

5つ目にして最後のファクトリーのティア。

## デザイン

プレビューで小さく表示される疑似スポナーはオプションであることに注意してください。

5つ目のティアに設定した<ItemImage id="layout" scale="0.5"/>[レイアウト](../machines-blocks/layout.md#iron)は建設をするのに役に立ちます。

<GameScene zoom="2.5" interactive={true}>
    <ImportStructure src="../assets/factory/netherite.snbt" />
    <IsometricCamera yaw="195" pitch="6" />
</GameScene>

## ネザライトティアのブロック
<Row>
  <BlockImage id="netherite_pylon" scale="4" p:attached="true" />
  <BlockImage id="netherite_plinth" scale="4" p:attached="true" />
  <BlockImage id="netherite_cell" scale="4" p:attached="true" />
</Row>


これには、
<ItemImage id="netherite_pylon" scale="0.5"/>ネザライトのパイロンが12個、
<ItemImage id="netherite_plinth" scale="0.5"/>ネザライトの土台が24個
<ItemImage id="netherite_cell" scale="0.5"/>ネザライトのバイタリティセルが1個必要です。

<Row>
  <RecipeFor id="netherite_pylon" />
  <RecipeFor id="netherite_plinth" />
  <RecipeFor id="netherite_cell" />
</Row>