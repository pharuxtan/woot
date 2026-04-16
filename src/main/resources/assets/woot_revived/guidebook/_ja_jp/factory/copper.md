---
navigation:
    parent: factory/factory-index.md
    title: "銅"
    icon: "woot_revived:copper_cell"
    position: 1
---
# 銅

ファクトリーの第一段階。これはファクトリーの最小バージョンです。

MODを始めるのにも、基本的なモブ1体用のファクトリーを作るのにも最適です!

## デザイン

まずファクトリーの中央を建設し、その後周囲の建設を完了させます。

最初のティアに設定した<ItemImage id="layout" scale="0.5"/>[レイアウト](../machines-blocks/layout.md#copper)は建設をするのに役に立ちます。

<Row>
    <GameScene zoom="2.5" interactive={true}>
        <ImportStructure src="../assets/factory/copper_half.snbt" />
        <IsometricCamera yaw="195" pitch="6" />
    </GameScene>

    <GameScene zoom="2.5" interactive={true}>
        <ImportStructure src="../assets/factory/copper.snbt" />
        <IsometricCamera yaw="195" pitch="6" />
    </GameScene>
</Row>

## 銅ティアのブロック

<Row>
  <BlockImage id="copper_pylon" scale="4" p:attached="true" />
  <BlockImage id="copper_plinth" scale="4" p:attached="true" />
  <BlockImage id="copper_cell" scale="4" p:attached="true" />
</Row>

工場には、[汎用ブロック](common-blocks.md)に加え、そのティア固有のブロックも必要です。

これには、
<ItemImage id="copper_pylon" scale="0.5"/>銅のパイロンが17個、
<ItemImage id="copper_plinth" scale="0.5"/>銅の土台が6個、
<ItemImage id="copper_cell" scale="0.5"/>銅のバイタリティセルが1個必要です。
<Row>
  <RecipeFor id="copper_pylon" />
  <RecipeFor id="copper_plinth" />
  <RecipeFor id="copper_cell" />
</Row>