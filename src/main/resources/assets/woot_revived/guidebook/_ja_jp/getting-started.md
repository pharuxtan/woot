---
navigation:
  title: "始め方"
  position: 10
  icon: "woot_revived:stygian_ingot"
---
# 始め方

## スティジアンインゴットを入手する
Woot Revivedを始めるには、まず<ItemImage id="stygian_dust" scale="0.5"/>スティジアンの粉を作る必要があります。
<Recipe id="stygian_dust" />

次にスティジアンの粉を精錬して<ItemImage id="stygian_ingot" scale="0.5"/>スティジアンインゴットを作りましょう。
<Recipe id="stygian_ingot_cook" />

## 金型を入手する

まず、<ItemImage id="stygian_anvil" scale="0.5"/>スティジアンの金床と<ItemImage id="stygian_hammer" scale="0.5"/>スティジアンのハンマーを作成する必要があります。
<Row>
    <Recipe id="stygian_anvil" />
    <Recipe id="stygian_hammer" />
</Row>

スティジアンの金床を<ItemImage id="minecraft:magma_block" scale="0.5"/>マグマブロックの上に置きます。

スティジアンの金床は、スティジアンハンマーで右クリックすることでクラフトを行えます。

それぞれの金型には<ItemImage id="minecraft:quartz" scale="0.5"/>ネザークォーツと<ItemImage id="stygian_ingot" scale="0.5"/>スティジアンインゴットが必要になります。

金型を使用してクラフトする際、金型は消費されません。必要なのは1つだけです。

<Row alignItems="center">
  <GameScene zoom="5">
    <ImportStructure src="assets/anvil/plate_mold.snbt" />
    <IsometricCamera yaw="180" pitch="40" />
  </GameScene>
  <ItemImage id="plate_mold" scale="2"/>
  板金の金型を作るには<ItemImage id="minecraft:iron_trapdoor" scale="0.5"/>鉄のトラップドアが必要です
</Row>

<Row alignItems="center">
  <GameScene zoom="5">
    <ImportStructure src="assets/anvil/shard_mold.snbt" />
    <IsometricCamera yaw="180" pitch="40" />
  </GameScene>
  <ItemImage id="shard_mold" scale="2"/>
  欠片の金型を作るには<ItemImage id="minecraft:prismarine_shard" scale="0.5"/>プリズマリンの欠片が必要です
</Row>

<Row alignItems="center">
  <GameScene zoom="5">
    <ImportStructure src="assets/anvil/dye_casing_mold.snbt" />
    <IsometricCamera yaw="180" pitch="40" />
  </GameScene>
  <ItemImage id="dye_casing_mold" scale="2"/>
  染料板の板の金型を作るには<ItemImage id="minecraft:white_dye" scale="0.5"/>何らかの染料が必要です
</Row>

## ファクトリーベースの作成

このMODのすべてのブロックを作成するには、ファクトリーベースをクラフトする必要があります。

まず<ItemImage id="stygian_plate" scale="0.5"/>スティジアンの板をクラフトしましょう

<Row alignItems="center">
  <GameScene zoom="5">
    <ImportStructure src="assets/anvil/stygian_plate.snbt" />
    <IsometricCamera yaw="180" pitch="40" />
  </GameScene>
  <ItemImage id="stygian_plate" scale="2"/>
  これを作るには<ItemImage id="plate_mold" scale="0.5"/>板金の金型と<ItemImage id="stygian_ingot" scale="0.5"/>スティジアンインゴットが必要になります
</Row>

すると、ファクトリーベースブロックを作成できるようになります。

<Recipe id="factory_base" />

## この後は?

[モブを記録](mob-shard.md)し始めることもできますし [最初のファクトリーを建設](factory/copper.md)し始めることもできますよ!