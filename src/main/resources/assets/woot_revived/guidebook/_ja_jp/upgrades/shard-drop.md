---
navigation:
    parent: upgrades/upgrades-index.md
    title: "欠片アップグレード"
    icon: "woot_revived:iron_shard_drop_upgrade"
    position: 1
---
# 欠片アップグレード

<Row>
  <ItemImage id="iron_shard_drop_upgrade" scale="3"/>
  <ItemImage id="gold_shard_drop_upgrade" scale="3"/>
  <ItemImage id="diamond_shard_drop_upgrade" scale="3"/>
  <ItemImage id="netherite_shard_drop_upgrade" scale="3"/>
</Row>

欠片アップグレードは、ファクトリーが各ティアの欠片を生産するようになります。

## 銅の欠片を入手するには

ご覧の通り、アップグレードでは<ItemImage id="copper_shard" scale="0.5"/>銅の欠片は生産されません。

<ItemImage id="copper_shard" scale="0.5"/>銅の欠片は<ItemImage id="stygian_anvil" scale="0.5"/>スティジアンの金床で作成する必要があるためです。

<Row alignItems="center">
  <GameScene zoom="5">
    <ImportStructure src="../assets/anvil/copper_shard.snbt" />
    <IsometricCamera yaw="180" pitch="40" />
  </GameScene>
  <ItemImage id="copper_shard" scale="2"/>
  これを作るには、<ItemImage id="shard_mold" scale="0.5"/>欠片の金型と<ItemImage id="minecraft:copper_ingot" scale="0.5"/>銅インゴット4つが必要です。
</Row>

## 欠片アップグレード I

<ItemImage id="iron_shard" scale="0.5"/>鉄の欠片を50%の確率で生産します。

<ItemImage id="iron_shard" scale="0.5"/>鉄の欠片をドロップするには、銅ティア以上のファクトリーにアップグレードを適用する必要があります。

<RecipeFor id="iron_shard_drop_upgrade" />

## 欠片アップグレード II

<ItemImage id="iron_shard" scale="0.5"/>鉄の欠片を50%、

<ItemImage id="gold_shard" scale="0.5"/>金の欠片を30%の確率で生産します。

<ItemImage id="gold_shard" scale="0.5"/>金の欠片をドロップするには、鉄ティア以上のファクトリーにアップグレードを適用する必要があります。

<RecipeFor id="gold_shard_drop_upgrade" />

## 欠片アップグレード III

<ItemImage id="iron_shard" scale="0.5"/>鉄の欠片を50%、<ItemImage id="gold_shard" scale="0.5"/> 金の欠片を30%、

<ItemImage id="diamond_shard" scale="0.5"/>ダイヤモンドの欠片を15％の確率で生産します。

<ItemImage id="diamond_shard" scale="0.5"/>ダイヤモンドの欠片をドロップするには、金ティア以上のファクトリーにアップグレードを適用する必要があります。

<RecipeFor id="diamond_shard_drop_upgrade" />

## 欠片アップグレード IV

<ItemImage id="iron_shard" scale="0.5"/>鉄の欠片を50%、<ItemImage id="gold_shard" scale="0.5"/>金の欠片を30%、<ItemImage id="diamond_shard" scale="0.5"/>ダイヤモンドの欠片を15％、

<ItemImage id="netherite_shard" scale="0.5"/>ネザライトの欠片を5%の確率で生産します。

<ItemImage id="netherite_shard" scale="0.5"/>ネザライトの欠片をドロップするには、ダイヤモンドティア以上のファクトリーにアップグレードを適用する必要があります。

<RecipeFor id="netherite_shard_drop_upgrade" />
